package Utils;

public class UsefulFunctions {

    public static double random(double n) {
        return Math.random() * n;
    }

    public static double random(double s, double e) {
        return s + Math.random() * (e - s);
    }

    public static int randInt(int n) {
        return (int) UsefulFunctions.random(n);
    }

    public static int randInt(int s, int e) {
        return (int) UsefulFunctions.random(s, e);
    }

    public static Vector2D randomVector(double x1, double x2, double y1, double y2) {
        return new Vector2D(UsefulFunctions.random(x1, x2), UsefulFunctions.random(y1, y2));
    }

    public static Vector2D randomVector(double x, double y, boolean positiveOnly) {
        if (positiveOnly) {
            return new Vector2D(UsefulFunctions.random(x), UsefulFunctions.random(y));
        }
        return UsefulFunctions.randomVector(x, y);
    }

    public static Vector2D randomVector(double x, double y) {
        return new Vector2D(UsefulFunctions.random(-x, x), UsefulFunctions.random(-y, y));
    }

    public static Vector2D randomVector(double r) {
        double theta = UsefulFunctions.random(2 * Math.PI);
        return Vector2D.vectorRTheta(r, theta);
    }

    public static Vector2D randomVector(double r, boolean positiveOnly) {
        double theta = UsefulFunctions.random(Math.PI / 2);
        return Vector2D.vectorRTheta(r, theta);
    }

    public static double degrees(double radians) {
        return radians / Math.PI * 180;
    }

    public static double radians(double degrees) {
        return degrees / 180 * Math.PI;
    }

}
