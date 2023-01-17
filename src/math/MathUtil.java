package math;

public class MathUtil {

    public static float fastInverseSquareRoot(float x) {
        return fastInverseSquareRoot(x, 1);
    }

    // https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
    public static float fastInverseSquareRoot(float x, int iterations) {
        float halfX = 0.5f * x;
        x = Float.intBitsToFloat(0x5f3759df - (Float.floatToIntBits(x) >> 1));

        for (int j = 0; j < iterations; j++) {
            x *= (1.5f - halfX * x * x);
        }

        return x;
    }
}
