package util;

public class StringUtil {
    public static <T> String arrayToString(T[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (int i = 0; i < arr.length - 1; i++) {
            stringBuilder.append(arr[i].toString());
            stringBuilder.append(", ");
        }

        stringBuilder.append(arr[arr.length - 1]);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public static String arrayToString(float[] arr) {
        Float[] objArr = new Float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            objArr[i] = arr[i];
        }
        return arrayToString(objArr);
    }
}
