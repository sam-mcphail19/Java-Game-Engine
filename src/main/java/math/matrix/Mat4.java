package math.matrix;

import lombok.Getter;
import math.vector.Vector3;
import math.vector.Vector4;

public class Mat4 {
    @Getter
    private double[] elements;

    private Mat4() {
        this.elements = new double[4 * 4];
    }

    public Mat4(double diagonal) {
        this();

        setVal(0, 0, diagonal);
        setVal(1, 1, diagonal);
        setVal(2, 2, diagonal);
        setVal(3, 3, diagonal);
    }

    public Mat4(double[] elements) {
        if (elements.length != 16) {
            throw new IllegalArgumentException();
        }

        this.elements = elements;
    }

    public static Mat4 identity() {
        return new Mat4(1.0);
    }

    public static Mat4 projection(double fov, double aspectRatio, double near, double far) {
        Mat4 result = Mat4.identity();

        double top = near * Math.tan(0.5 * Math.toRadians(fov));
        double bottom = -top;
        double right = top * aspectRatio;
        double left = -right;

        result.setVal(0, 0, 2 * near / (right - left));
        result.setVal(0, 2, (right + left) / (right - left));
        result.setVal(1, 1, 2 * near / (top - bottom));
        result.setVal(1, 2, (top + bottom) / (top - bottom));
        result.setVal(2, 2, (-far + near) / (far - near));
        result.setVal(2, 3, -2 * far * near / (far - near));
        result.setVal(3, 2, -1);
        result.setVal(3, 3, 0);

        return result;
    }

    public static Mat4 translate(Vector3 translation) {
        Mat4 result = Mat4.identity();

        result.setVal(0, 3, translation.getX());
        result.setVal(1, 3, translation.getY());
        result.setVal(2, 3, translation.getZ());

        return result;
    }

    public static Mat4 xRotation(double angleInDegrees) {
        double angle = Math.toRadians(angleInDegrees);
        Mat4 mat = Mat4.identity();
        mat.setVal(1, 1, Math.cos(angle));
        mat.setVal(1, 2, -Math.sin(angle));
        mat.setVal(2, 1, Math.sin(angle));
        mat.setVal(2, 2, Math.cos(angle));

        return mat;
    }

    public static Mat4 yRotation(double angleInDegrees) {
        double angle = Math.toRadians(angleInDegrees);
        Mat4 mat = Mat4.identity();
        mat.setVal(0, 0, Math.cos(angle));
        mat.setVal(0, 2, Math.sin(angle));
        mat.setVal(2, 0, -Math.sin(angle));
        mat.setVal(2, 2, Math.cos(angle));

        return mat;
    }

    public static Mat4 zRotation(double angleInDegrees) {
        double angle = Math.toRadians(angleInDegrees);
        Mat4 mat = Mat4.identity();
        mat.setVal(0, 0, Math.cos(angle));
        mat.setVal(0, 1, -Math.sin(angle));
        mat.setVal(1, 0, Math.sin(angle));
        mat.setVal(1, 1, Math.cos(angle));

        return mat;
    }

    public static Mat4 scale(Vector3 scale) {
        Mat4 result = Mat4.identity();

        result.setVal(0, 0, scale.getX());
        result.setVal(1, 1, scale.getY());
        result.setVal(2, 2, scale.getZ());

        return result;
    }

    public Mat4 multiply(Mat4 other) {
        double[] result = new double[4 * 4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                double sum = 0;
                for (int i = 0; i < 4; i++) {
                    sum += getVal(row, i) * other.getVal(i, col);
                }
                result[getIndex(row, col)] = sum;
            }
        }

        return new Mat4(result);
    }

    public Vector4 multiply(Vector4 vec) {
        double[] result = new double[4];
        for (int row = 0; row < 4; row++) {
            result[row] = getVal(row, 0) * vec.getX() +
                getVal(row, 1) * vec.getY() +
                getVal(row, 2) * vec.getZ() +
                getVal(row, 3) * vec.getW();
        }
        return new Vector4(result[0], result[1], result[2], result[3]);
    }

    public Mat4 transpose() {
        Mat4 result = new Mat4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.setVal(j, i, this.getVal(i, j));
            }
        }
        return result;
    }

    public void setRow(Vector4 vec, int row) {
        setVal(row, 0, vec.getX());
        setVal(row, 0, vec.getY());
        setVal(row, 0, vec.getZ());
        setVal(row, 0, vec.getW());
    }

    public float[] getElementsAsF() {
        float[] floatArray = new float[elements.length];
        for (int i = 0; i < elements.length; i++) {
            floatArray[i] = (float) elements[i];
        }
        return floatArray;
    }

    public double getVal(int row, int col) {
        return elements[getIndex(row, col)];
    }

    public void setVal(int row, int col, double val) {
        elements[getIndex(row, col)] = val;
    }

    public Mat4 copy() {
        return new Mat4(elements.clone());
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
