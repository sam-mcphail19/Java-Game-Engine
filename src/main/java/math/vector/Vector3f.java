package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import math.MathUtil;


@Data
@AllArgsConstructor
public class Vector3f {

    private float x;
    private float y;
    private float z;

    public Vector3f(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    public Vector3f normalize() {
        float magnitudeSquared = (float) (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        if (Math.abs(1 - magnitudeSquared) < 0.0001) {
            return this;
        }
        float inverseSquareRoot = MathUtil.fastInverseSquareRoot(magnitudeSquared, 4);

        return this.multiply(inverseSquareRoot);
    }

    public Vector3f add(Vector3f other) {
        x += other.getX();
        y += other.getY();
        z += other.getZ();
        return this;
    }

    public Vector3f subtract(Vector3f other) {
        x -= other.getX();
        y -= other.getY();
        z -= other.getZ();
        return this;
    }

    public float dot(Vector3f other) {
        float sum = 0;
        sum += x * other.getX();
        sum += y * other.getY();
        sum += z * other.getZ();
        return sum;
    }

    public Vector3f cross(Vector3f other) {
        return new Vector3f(
            y * other.getZ() - z * other.getY(),
            x * other.getZ() - z * other.getX(),
            x * other.getY() - y * other.getX()
        );
    }

    public Vector3f multiply(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;
        return this;
    }

    public static Vector3f xAxis() {
        return new Vector3f(1, 0, 0);
    }

    public static Vector3f yAxis() {
        return new Vector3f(0, 1, 0);
    }

    public static Vector3f zAxis() {
        return new Vector3f(0, 0, 1);
    }

    public Vector3f copy() {
        return new Vector3f(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3f(" + x + ", " + y + ", " + z + ")";
    }
}
