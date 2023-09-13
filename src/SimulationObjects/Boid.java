package SimulationObjects;

import ParameterSettings.Parameters;
import Utils.BoidColors;
import Utils.UsefulFunctions;
import Utils.Vector2D;

public class Boid extends SimulationObject {
    private final boolean isPredator;
    private Vector2D velocity;

    public Boid() {
        this(new Vector2D(UsefulFunctions.randomVector(Parameters.panelWidth,
                        Parameters.panelHeight, true)), new Vector2D(),
                BoidColors.BLUE);
    }

    public Boid(Vector2D position, Vector2D velocity, BoidColors color) {
        super(position, true);
        this.velocity = velocity;
        this.color = color.getValue();
        this.myColor = color;
        this.isPredator = false;
    }

    public Boid(Boid other) {
        super(other.getPosition(), true);
        this.velocity = other.velocity;
        this.color = other.color;
        this.myColor = other.myColor;
        this.isPredator = other.isPredator;
    }

    public Boid(Vector2D position, Vector2D velocity, BoidColors color, boolean isPredator) {
        super(position, true);
        this.velocity = velocity;
        this.isPredator = isPredator;
        this.color = color.getValue();
        this.myColor = color;
        if (isPredator) {
            this.color = BoidColors.RED.getValue();
            this.myColor = BoidColors.RED;
        }
    }

    public boolean isPredator() {
        return isPredator;
    }

    public void updateVel(Vector2D Force) {
        this.velocity = this.getVelocity().add(Force.multiply((double) Parameters.deltaT / 1000));
        if (this.velocity.x > 0) {
            this.velocity.x += 1;
        }
        if (this.velocity.y > 0) {
            this.velocity.y += 1;
        }
        if (Math.random() < 0.1) {
            this.velocity = this.getVelocity().add(UsefulFunctions.randomVector(0.02));
        }
        if (this.velocity.length() < Parameters.minVelocity) {
            this.velocity = this.velocity.normalized().multiply(Parameters.minVelocity);
        } else if (this.velocity.length() > Parameters.maxVelocity) {
            this.velocity = this.velocity.normalized().multiply(Parameters.maxVelocity);
        }
    }

    public void updatePos() {
        oldHashIndex = hashIndex;
        this.position.x += this.getVelocity().x * Parameters.deltaT / 1000;
        this.position.y += this.getVelocity().y * Parameters.deltaT / 1000;

        if (Parameters.wrapAround) {

            if (this.getPosition().x > Parameters.panelWidth) {
                this.position.x -= Parameters.panelWidth;
            }
            if (this.getPosition().y > Parameters.panelHeight) {
                this.position.y -= Parameters.panelHeight;
            }
            if (this.getPosition().x < 0) {
                this.position.x += Parameters.panelWidth;
            }
            if (this.getPosition().y < 0) {
                this.position.y += Parameters.panelHeight;
            }
        } else {
            if (this.getX() > Parameters.panelWidth - Parameters.marginWidth) {
                this.updateVel(new Vector2D(-(this.getVelocity().length() / (Parameters.panelWidth - this.getX() + 1)) * Parameters.edgeRepulsion, 0));
                if (this.getX() >= Parameters.panelWidth) {
                    this.position.x = Parameters.panelWidth - 1;
                    this.velocity = this.getVelocity().multiply(-0.5);
                    //just to avoid edge cases where you can get a hash idx of 'no.of boxes' which raises indexoutofboundsexception
                }
            }
            if (this.getY() > Parameters.panelHeight - Parameters.marginWidth) {
                this.updateVel(new Vector2D(0,
                        -(this.getVelocity().length() / (Parameters.panelHeight - this.getY() + 1)) * Parameters.edgeRepulsion));
                if (this.getY() >= Parameters.panelHeight) {
                    this.position.y = Parameters.panelHeight - 1;
                    this.velocity = this.getVelocity().multiply(-0.5);
                }
            }
            if (this.getX() < Parameters.marginWidth) {
                this.updateVel(new Vector2D((this.getVelocity().length() / (this.getX() + 1)) * Parameters.edgeRepulsion, 0));
                if (this.getX() <= 0) {
                    this.position.x = 0;
                    this.velocity = this.getVelocity().multiply(-0.5);
                }
            }
            if (this.getY() < Parameters.marginWidth) {
                this.updateVel(new Vector2D(0, (this.getVelocity().length() / (this.getY() + 1)) * Parameters.edgeRepulsion));
                if (this.getY() <= 0) {
                    this.position.y = 0;
                    this.velocity = this.getVelocity().multiply(-0.5);
                }
            }
        }
        updateHashIndex();
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public int[][] getBodyCoordinates() {
        Vector2D heading = this.getVelocity().normalized();
        if (heading.length() == 0) {
            heading = new Vector2D(1, 0);
        }
        Vector2D nose = this.getPosition().add(heading.multiply(Parameters.boidSize));
        Vector2D left =
                this.getPosition().add(heading.rotated(-135).multiply(Parameters.boidSize * 0.65));
        Vector2D right =
                this.getPosition().add(heading.rotated(135).multiply(Parameters.boidSize * 0.65));

        return new int[][]{{(int) nose.x, (int) right.x, (int) this.getX(), (int) left.x}, {(int) nose.y, (int) right.y,
                (int) this.getY(), (int) left.y}};
    }

    public boolean isVisible(SimulationObject other) {
        Vector2D directionVector = this.getDirectionVector(other);
        return this.getVelocity().angle(directionVector) <= Parameters.perceptionAngle / 2
                &&
                directionVector.length() <= Parameters.perceptionRadius;
    }

    @Override
    public String toString() {
        if (isPredator) {
            return "Boid*{" +
                    "position=" + position.round(2) +
                    ", velocity=" + velocity.round(2) +
                    '}';
        }
        return "Boid{" +
                "position=" + position.round(2) +
                ", velocity=" + velocity.round(2) +
                ", color=" + myColor +
                '}';
    }

    @Override
    public void update(Vector2D Force) {
        this.updateVel(Force);
        this.updatePos();
    }
}
