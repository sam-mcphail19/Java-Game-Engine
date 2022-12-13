package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Vector3f {

    private float x;
    private float y;
    private float z;

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

    @Override
    public String toString() {
        return "Vector3f(" + x + ", " + y + ", " + z + ")";
    }
}
