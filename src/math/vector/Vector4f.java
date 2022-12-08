package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Vector4f {

    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f add(Vector4f other) {
        x += other.getX();
        y += other.getY();
        z += other.getZ();
        w += other.getW();
        return this;
    }

    public Vector4f subtract(Vector4f other) {
        x -= other.getX();
        y -= other.getY();
        z -= other.getZ();
        w -= other.getW();
        return this;
    }

    public float dot(Vector4f other) {
        float sum = 0;
        sum += x * other.getX();
        sum += y * other.getY();
        sum += z * other.getZ();
        sum += w * other.getW();
        return sum;
    }
}
