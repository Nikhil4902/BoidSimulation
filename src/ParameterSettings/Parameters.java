package ParameterSettings;

import Utils.BoidColors;
import Utils.ObjectType;
import Utils.UsefulFunctions;

public class Parameters {
    public static final int deltaT = 10;

    public static final int panelWidth = 1425;
    public static final int panelHeight = 770;
    public static final int marginWidth = Math.min(panelHeight, panelWidth) / 4;

    public static final int boidSize = 10;
    public static final double perceptionRadius = 70;
    public static final int numHorizontalBoxes = (int) Math.ceil(panelWidth / perceptionRadius);
    public static final int numVerticalBoxes = (int) Math.ceil(panelHeight / perceptionRadius);
    public static final double perceptionAngle = UsefulFunctions.radians(360);
    public static final double boidInitialVelocity = 300;
    public static final double minVelocity = 250;
    public static final double maxVelocity = 350;
    public static final double edgeRepulsion = 600;
    public static final boolean wrapAround = false;
    public static int numTypes = 1;
    public static int numBoids = 200;
    public static int numPredators = 10;
    public static int numObstacles = 0;
    public static double separationStrength = 700;
    public static double alignmentStrength = 30;
    public static double cohesionStrength = 100;
    public static double predatorRepulsionStrength = 10;
    public static double obstacleRepulsionStrength = 80;
    public static double boidAttractionStrength = 500;
    public static double selfRepulsionStrength = 50;
    public static boolean isRunning = true;
    public static boolean showGrid = false;
    public static BoidColors currentColor = BoidColors.BLUE;
    public static ObjectType currentObject = ObjectType.BOID;
}
