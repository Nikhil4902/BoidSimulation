package SimulationObjects;

import ParameterSettings.Parameters;
import Utils.BoidColors;
import Utils.SpacialHash;
import Utils.UsefulFunctions;
import Utils.Vector2D;

import java.util.ArrayList;

public class School {
    public static ArrayList<SimulationObject> simulationObjects = new ArrayList<>();
    public static int schoolSize = 0;

    public static void createSchool() {
        for (int i = 0; i < Parameters.numBoids; i++) {
            addObject(new Boid(UsefulFunctions.randomVector(Parameters.panelWidth, Parameters.panelHeight,
                    true), UsefulFunctions.randomVector(Parameters.boidInitialVelocity),
                    BoidColors.values()[UsefulFunctions.randInt(Parameters.numTypes)]));
        }

        for (int i = 0; i < Parameters.numPredators; i++) {
            addObject(new Boid(UsefulFunctions.randomVector(Parameters.panelWidth, Parameters.panelHeight, true),
                    UsefulFunctions.randomVector(Parameters.boidInitialVelocity),
                    BoidColors.RED, true));
        }
        for (int i = 0; i < Parameters.numObstacles; i++) {
            addObject(new Obstacle(UsefulFunctions.randomVector(Parameters.panelWidth,
                    Parameters.panelHeight, true)));
        }
    }

    public static void update() {
        Vector2D[] Forces = new Vector2D[simulationObjects.size()];
        for (int mainObjIdx = 0; mainObjIdx < simulationObjects.size(); mainObjIdx++) {
            Forces[mainObjIdx] = new Vector2D();
            SimulationObject mainObj = simulationObjects.get(mainObjIdx);
            if (mainObj.isBoid()) {
                Boid mainBoid = (Boid) mainObj;
                ArrayList<Integer> neighbours = mainBoid.getNeighbours();
                if (neighbours.size() > 1) {
                    if (mainBoid.isPredator()) {
                        //main object is a predator boid
                        Vector2D meanBoidPos = new Vector2D();
                        Vector2D meanPredatorSep = new Vector2D();
                        Vector2D meanObstacleSep = new Vector2D();
                        for (Integer neighbourIdx : neighbours) {
                            SimulationObject neighbourObj = simulationObjects.get(neighbourIdx);
                            if (neighbourIdx != mainObjIdx && mainBoid.isVisible(neighbourObj)) {
                                Vector2D directionVector = neighbourObj.getDirectionVector(mainBoid);
                                if (neighbourObj.isBoid()) {
                                    if (((Boid) neighbourObj).isPredator()) {
                                        meanPredatorSep = meanPredatorSep.add(directionVector.multiply(Math.pow(1.2, -directionVector.length())));
                                    } else {
                                        meanBoidPos = meanBoidPos.add(neighbourObj.getPosition());
                                    }
                                } else {
                                    meanObstacleSep = meanObstacleSep.add(directionVector.multiply(Math.pow(1.2, -directionVector.length())));
                                }
                            }
                        }
//                        meanBoidPos = meanBoidPos.length() != 0 ? meanBoidPos.divide(neighbours.size()) : mainBoid.getPosition();
                        Vector2D meanPosPointingVector = meanBoidPos.length() != 0 ?
                                meanBoidPos.divide(neighbours.size() - 1).subtract(mainBoid.getPosition()) : new Vector2D(0, 0);
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanPosPointingVector.multiply(Parameters.boidAttractionStrength / 10));
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanPredatorSep.multiply(Parameters.selfRepulsionStrength * 100));
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanObstacleSep.multiply(Parameters.obstacleRepulsionStrength * 1000));
                    } else {
                        //main object is a regular boid
                        Vector2D meanPos = new Vector2D();
                        Vector2D meanVel = new Vector2D();
                        Vector2D meanSep = new Vector2D();
                        Vector2D meanPredatorSep = new Vector2D();
                        Vector2D meanObstacleSep = new Vector2D();
                        for (Integer neighbourIdx : neighbours) {
                            SimulationObject neighbourObj = simulationObjects.get(neighbourIdx);
                            if (neighbourIdx != mainObjIdx && mainBoid.isVisible(neighbourObj)) {
                                Vector2D directionVector = neighbourObj.getDirectionVector(mainBoid);
                                if (neighbourObj.isBoid()) {
                                    Boid neighbourBoid = (Boid) neighbourObj;
                                    if (neighbourBoid.isPredator()) {
                                        //neighbouring object is a predator boid
                                        meanPredatorSep = meanPredatorSep.add(directionVector.multiply(Math.pow(1.2,
                                                -directionVector.length())));
                                    } else {
                                        if (mainBoid.getBoidColor() == neighbourBoid.getBoidColor()) {
                                            //neighbouring object is a regular boid of the same colour
                                            meanPos =
                                                    meanPos.add(neighbourBoid.getPosition().multiply(1 / (directionVector.length() + 1)));
                                            meanVel = meanVel.add(neighbourBoid.getVelocity());
                                            if (directionVector.length() < 0.8 * Parameters.perceptionRadius) {
                                                meanSep = meanSep.add(directionVector.divide(Math.pow(directionVector.length(), 2) +
                                                        Math.pow(10, -2)));
                                            }
                                        } else {
                                            //neighbouring object is a regular boid of a different color
                                            if (directionVector.length() < 0.6 * Parameters.perceptionRadius) {
                                                meanSep =
                                                        meanSep.add(directionVector.divide(Math.pow(directionVector.length(),
                                                                2) * 2 + Math.pow(10, -2)));
                                            }
                                        }
                                    }
                                } else {
                                    //neighbouring object is an obstacle
                                    meanObstacleSep = meanObstacleSep.add(directionVector.multiply(Math.pow(1.2,
                                            -directionVector.length())));
                                }
                            }
                        }
                        meanPos = meanPos.length() != 0 ? meanPos.divide(neighbours.size() - 1) : mainBoid.getPosition();
                        Vector2D meanPosPointingVector = meanPos.subtract(mainBoid.getPosition());
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanPosPointingVector.multiply(Parameters.cohesionStrength / 1000));
                        meanVel = meanVel.length() != 0 ? meanVel.divide(neighbours.size() - 1) : mainBoid.getVelocity();
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanVel.subtract(mainBoid.getVelocity()).multiply(Parameters.alignmentStrength));
                        Forces[mainObjIdx] = Forces[mainObjIdx].add(meanSep.multiply(Parameters.separationStrength));
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanObstacleSep.multiply(Parameters.obstacleRepulsionStrength * 1000));
                        Forces[mainObjIdx] =
                                Forces[mainObjIdx].add(meanPredatorSep.multiply(Parameters.predatorRepulsionStrength * 10000000));
                    }
                }
            }
        }

        for (int i = 0; i < simulationObjects.size(); i++) {
            simulationObjects.get(i).update(Forces[i]);
            SpacialHash.updateHashTable(i, simulationObjects.get(i).getOldHashIndex(),
                    simulationObjects.get(i).getHashIndex());
        }
    }

    public static ArrayList<SimulationObject> getObjects() {
        return simulationObjects;
    }

    public static void addObject(SimulationObject object) {
        simulationObjects.add(object);
        SpacialHash.addToHashTable(schoolSize, object.getHashIndex());
        schoolSize++;
    }

}

