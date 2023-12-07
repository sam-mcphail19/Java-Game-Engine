package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import math.MathUtil;


@Data
@AllArgsConstructor
public class Vector3 {

    private double x;
    private double y;
    private double z;

    public Vector3() {
        this(0);
    }

    public Vector3(double value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    public Vector3 normalize() {
        double magnitudeSquared = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
        if (Math.abs(1 - magnitudeSquared) < 0.0001) {
            return this;
        }
        double inverseSquareRoot = MathUtil.fastInverseSquareRoot(magnitudeSquared, 4);

        return multiply(inverseSquareRoot);
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public double dot(Vector3 other) {
        return x * other.getX() + y * other.getY() + z * other.getZ();
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(
            y * other.getZ() - z * other.getY(),
            x * other.getZ() - z * other.getX(),
            x * other.getY() - y * other.getX()
        );
    }

    public Vector3 multiply(double factor) {
        return new Vector3(x * factor, y * factor, z * factor);
    }

    public Vector4 toVec4() {
        return new Vector4(this.x, this.y, this.z, 1);
    }

    public static Vector3 xAxis() {
        return new Vector3(1, 0, 0);
    }

    public static Vector3 yAxis() {
        return new Vector3(0, 1, 0);
    }

    public static Vector3 zAxis() {
        return new Vector3(0, 0, 1);
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }
}
