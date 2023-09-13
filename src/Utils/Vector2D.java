package Utils;

public class Vector2D {
    public double x, y;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public static Vector2D vectorRTheta(double r, double theta) {
        return new Vector2D(r * Math.cos(theta), r * Math.sin(theta));
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public Vector2D divide(double scalar) throws ArithmeticException {
        if (scalar == 0) {
            throw new ArithmeticException("Zero division error");
        }
        return new Vector2D(this.x / scalar, this.y / scalar);
    }

    public void normalize() {
        double len = this.length();
        this.x /= len;
        this.y /= len;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2D normalized() {
        double len = this.length();
        if (len == 0) {
            return new Vector2D();
        }
        return new Vector2D(this.x / len, this.y / len);
    }

    public double angleAC() {//with (1,0) anti clock-wise
        if (y == 0) {
            if (x == 0) {
                return 0;
            }
            return Math.PI;
        }
        return Math.acos(this.x / this.length()) + y < 0 ? Math.PI : 0;
    }

    public double slope() {
        return y != 0 ? Math.acos(this.x / this.length()) : 0;
    }

    public double angle(Vector2D other) {
        return Math.acos(this.dot(other) / (this.length() * other.length()));
    }

    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public void rotate(double theta) {
        double rad = theta * Math.PI / 180;
        double tx, ty;
        tx = this.x * Math.cos(rad) - this.y * Math.sin(rad);
        ty = this.x * Math.sin(rad) + this.y * Math.cos(rad);
        this.x = tx;
        this.y = ty;
    }

    public Vector2D rotated(double theta) {
        double rad = theta * Math.PI / 180;
        double tx, ty;
        tx = this.x * Math.cos(rad) - this.y * Math.sin(rad);
        ty = this.x * Math.sin(rad) + this.y * Math.cos(rad);
        return new Vector2D(tx, ty);
    }

    public Vector2D round(int n) {
        return new Vector2D((double) (int) (Math.pow(10, n) * this.x) / (int) Math.pow(10, n),
                (double) (int) (Math.pow(10, n) * this.y) / (int) Math.pow(10, n));
    }

    @Override
    public String toString() {
        return "<" + this.x + ", " + this.y + ">";
    }
}
