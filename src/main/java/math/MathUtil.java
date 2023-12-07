package math;

public class MathUtil {

    public static double fastInverseSquareRoot(double x) {
        return fastInverseSquareRoot(x, 1);
    }

    // https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
    public static double fastInverseSquareRoot(double x, int iterations) {
        double halfX = 0.5 * x;
        x = Float.intBitsToFloat(0x5f3759df - (Float.floatToIntBits((float) x) >> 1));

        for (int j = 0; j < iterations; j++) {
            x *= (1.5 - halfX * x * x);
        }

        return x;
    }
}
