package SimulationObjects;

import ParameterSettings.Parameters;
import Utils.BoidColors;
import Utils.SpacialHash;
import Utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class SimulationObject {
    private final boolean isBoid;
    protected Vector2D position;
    protected int oldHashIndex;
    protected int hashIndex;
    protected Color color = Color.WHITE;
    protected BoidColors myColor = BoidColors.WHITE;

    public SimulationObject(Vector2D position, boolean isBoid) {
        this.position = position;
        this.isBoid = isBoid;
        this.updateHashIndex();
        this.oldHashIndex = hashIndex;
    }

    @Override
    public String toString() {
        return "SimulationObject{" +
                "isBoid=" + isBoid +
                ", position=" + position.round(2) +
                ", myColor=" + myColor +
                '}';
    }

    public void updateHashIndex() {
        this.hashIndex = (int) (this.getY()) / (int) (Parameters.perceptionRadius) * Parameters.numHorizontalBoxes + (int) (this.getX()) / (int) (Parameters.perceptionRadius);
    }

    public double getY() {
        return this.position.y;
    }

    public double getX() {
        return this.position.x;
    }

    public int getOldHashIndex() {
        return oldHashIndex;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(BoidColors color) {
        this.color = color.getValue();
        this.myColor = color;
    }

    public BoidColors getBoidColor() {
        return myColor;
    }

    public void update(Vector2D Force) {
    }

    public boolean isBoid() {
        return isBoid;
    }

    public ArrayList<Integer> getNeighbours() {
        ArrayList<Integer> ans = new ArrayList<>();
        int hashIdx = getHashIndex();
        if (Parameters.wrapAround) {
            ans.addAll(SpacialHash.getObjectsInCell(this.upLeft(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(this.up(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(this.upRight(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(this.left(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(hashIdx));
            ans.addAll(SpacialHash.getObjectsInCell(this.right(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(this.downLeft(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(this.down(hashIdx)));
            ans.addAll(SpacialHash.getObjectsInCell(this.downRight(hashIdx)));

        } else {
            if (this.upLeft(hashIdx) < hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.upLeft(hashIdx)));
            }
            if (this.up(hashIdx) < hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.up(hashIdx)));
            }
            if (this.upRight(hashIdx) < hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.upRight(hashIdx)));
            }
            if (this.left(hashIdx) < hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.left(hashIdx)));
            }
            ans.addAll(SpacialHash.getObjectsInCell(hashIdx));
            if (this.right(hashIdx) > hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.right(hashIdx)));
            }
            if (this.downLeft(hashIdx) > hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.downLeft(hashIdx)));
            }
            if (this.down(hashIdx) > hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.down(hashIdx)));
            }
            if (this.downRight(hashIdx) > hashIdx) {
                ans.addAll(SpacialHash.getObjectsInCell(this.downRight(hashIdx)));
            }

        }
        return ans;
    }

    public int getHashIndex() {
        return hashIndex;
    }

    private int upLeft(int hashIdx) {
        return ((hashIdx / Parameters.numHorizontalBoxes - 1 + Parameters.numVerticalBoxes) % Parameters.numVerticalBoxes) * Parameters.numHorizontalBoxes + (hashIdx % Parameters.numHorizontalBoxes - 1 + Parameters.numHorizontalBoxes) % Parameters.numHorizontalBoxes;
    }

    private int up(int hashIdx) {
        return ((hashIdx / Parameters.numHorizontalBoxes - 1 + Parameters.numVerticalBoxes) % Parameters.numVerticalBoxes) * Parameters.numHorizontalBoxes + hashIdx % Parameters.numHorizontalBoxes;
    }

    private int upRight(int hashIdx) {
        return ((hashIdx / Parameters.numHorizontalBoxes - 1 + Parameters.numVerticalBoxes) % Parameters.numVerticalBoxes) * Parameters.numHorizontalBoxes + (hashIdx % Parameters.numHorizontalBoxes + 1) % Parameters.numHorizontalBoxes;
    }

    private int left(int hashIdx) {
        return (hashIdx / Parameters.numHorizontalBoxes) * Parameters.numHorizontalBoxes + (hashIdx % Parameters.numHorizontalBoxes - 1 + Parameters.numHorizontalBoxes) % Parameters.numHorizontalBoxes;
    }

    private int right(int hashIdx) {
        return (hashIdx / Parameters.numHorizontalBoxes) * Parameters.numHorizontalBoxes + (hashIdx % Parameters.numHorizontalBoxes + 1) % Parameters.numHorizontalBoxes;
    }

    private int downLeft(int hashIdx) {
        return ((hashIdx / Parameters.numHorizontalBoxes + 1) % Parameters.numVerticalBoxes) * Parameters.numHorizontalBoxes + (hashIdx % Parameters.numHorizontalBoxes - 1 + Parameters.numHorizontalBoxes) % Parameters.numHorizontalBoxes;
    }

    private int down(int hashIdx) {
        return ((hashIdx / Parameters.numHorizontalBoxes + 1) % Parameters.numVerticalBoxes) * Parameters.numHorizontalBoxes + hashIdx % Parameters.numHorizontalBoxes;
    }

    private int downRight(int hashIdx) {
        return ((hashIdx / Parameters.numHorizontalBoxes + 1) % Parameters.numVerticalBoxes) * Parameters.numHorizontalBoxes + (hashIdx % Parameters.numHorizontalBoxes + 1) % Parameters.numHorizontalBoxes;
    }

    public Vector2D getDirectionVector(SimulationObject other) {

        Vector2D ans;

        if (Parameters.wrapAround) {
            double x1 = other.getX() - this.getX();
            double x2 = x1 + Parameters.panelWidth;
            double x3 = x1 - Parameters.panelWidth;
            double y1 = other.getY() - this.getY();
            double y2 = y1 + Parameters.panelHeight;
            double y3 = y1 - Parameters.panelHeight;
            double x = minAbs(new double[]{x1, x2, x3});
            double y = minAbs(new double[]{y1, y2, y3});
            ans = new Vector2D(x, y);
        } else {
            ans = new Vector2D(other.getPosition().subtract(this.getPosition()));
        }

        return ans;
    }

    private double minAbs(double[] arr) {
        double ans = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (Math.abs(arr[i]) < Math.abs(ans)) {
                ans = arr[i];
            }
        }
        return ans;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        oldHashIndex = hashIndex;
        updateHashIndex();
    }

}
