package math.matrix;

import lombok.Getter;
import math.vector.Vector3f;
import math.vector.Vector4f;


public class Mat4f {
    @Getter
    private float[] elements;

    public Mat4f() {
        this.elements = new float[4 * 4];
    }

    public Mat4f(float diagonal) {
        this.elements = new float[4 * 4];

        setVal(0, 0, diagonal);
        setVal(1, 1, diagonal);
        setVal(2, 2, diagonal);
        setVal(3, 3, diagonal);
    }

    public Mat4f(float[] elements) {
        if (elements.length != 16) {
            throw new IllegalArgumentException();
        }

        this.elements = elements;
    }

    public static Mat4f identity() {
        return new Mat4f(1.0f);
    }

    public static Mat4f projection(float fov, float aspectRatio, float nearClip, float farClip) {
        Mat4f result = Mat4f.identity();

        float q = 1f / (float) Math.tan(Math.toRadians(0.5f * fov)) * aspectRatio;
        float a = q / aspectRatio;
        float frustumDepth = farClip - nearClip;

        result.setVal(0, 0, a);
        result.setVal(1, 1, q);
        result.setVal(2, 2, -((nearClip + farClip) / frustumDepth));
        result.setVal(2, 3, -1f);
        result.setVal(3, 2, -((2f * farClip * nearClip) / frustumDepth));

        return result;
    }

    public static Mat4f translate(Vector3f translation) {
        Mat4f result = Mat4f.identity();

        result.setVal(0, 3, translation.getX());
        result.setVal(1, 3, translation.getY());
        result.setVal(2, 3, translation.getZ());

        return result;
    }

    // TODO Quaternions
    public static Mat4f rotate(float angle, Vector3f axis) {
        Mat4f result = Mat4f.identity();

        double angleInRads = Math.toRadians(angle);
        axis.normalize();

        float cos = (float) Math.cos(angleInRads);
        float sin = (float) Math.sin(angleInRads);
        float oneMinusCos = 1 - cos;

        float x = axis.getX();
        float y = axis.getY();
        float z = axis.getZ();

        result.setVal(0, 0, x * oneMinusCos + cos);
        result.setVal(0, 1, y * x * oneMinusCos + z * sin);
        result.setVal(0, 2, x * z * oneMinusCos - y * sin);

        result.setVal(1, 0, x * y * oneMinusCos - z * sin);
        result.setVal(1, 1, y * oneMinusCos + cos);
        result.setVal(1, 2, y * z * oneMinusCos + x * sin);

        result.setVal(2, 0, x * z * oneMinusCos + y * sin);
        result.setVal(2, 1, y * z * oneMinusCos - x * sin);
        result.setVal(2, 2, z * oneMinusCos + cos);

        return result;
    }

    public static Mat4f scale(Vector3f scale) {
        Mat4f result = Mat4f.identity();

        result.setVal(0, 0, scale.getX());
        result.setVal(1, 1, scale.getY());
        result.setVal(2, 2, scale.getZ());

        return result;
    }

    public Mat4f multiply(Mat4f other) {
        float[] result = new float[4 * 4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sum = 0f;
                for (int i = 0; i < 4; i++) {
                    sum += getVal(row, i) * other.getVal(i, col);
                }
                result[getIndex(row, col)] = sum;
            }
        }

        this.elements = result;
        return this;
    }

    public Vector4f multiply(Vector4f vec) {
        float[] result = new float[4];
        for (int row = 0; row < 4; row++) {
            result[row] = getVal(row, 0) * vec.getX() +
                getVal(row, 1) * vec.getY() +
                getVal(row, 2) * vec.getZ() +
                getVal(row, 3) * vec.getW();
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }

    public Mat4f add(Mat4f other) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                setVal(row, col, getVal(row, col) + other.getVal(row, col));
            }
        }

        return this;
    }

    public void setRow(Vector4f vec, int row) {
        setVal(row, 0, vec.getX());
        setVal(row, 0, vec.getY());
        setVal(row, 0, vec.getZ());
        setVal(row, 0, vec.getW());
    }

    public float getVal(int row, int col) {
        return elements[getIndex(row, col)];
    }

    public void setVal(int row, int col, float val) {
        elements[getIndex(row, col)] = val;
    }

    public Mat4f copy(){
        return new Mat4f(elements.clone());
    }

    @Override
    public String toString() {
        return rowToString(0) + rowToString(1) + rowToString(2) + rowToString(3);
    }

    private int getIndex(int row, int col) {
        return col + row * 4;
    }

    private String rowToString(int row) {
        return "|" +
            String.format("%.03f", getVal(row, 0)) + ", " +
            String.format("%.03f", getVal(row, 1)) + ", " +
            String.format("%.03f", getVal(row, 2)) + ", " +
            String.format("%.03f", getVal(row, 3)) +
            "|\n";
    }
}
