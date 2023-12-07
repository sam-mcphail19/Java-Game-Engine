package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector4 {

    private double x;
    private double y;
    private double z;
    private double w;

    public Vector4(double val) {
        this.x = val;
        this.y = val;
        this.z = val;
        this.w = val;
    }

    public Vector4 add(Vector4 other) {
        x += other.getX();
        y += other.getY();
        z += other.getZ();
        w += other.getW();
        return this;
    }

    public Vector4 subtract(Vector4 other) {
        x -= other.getX();
        y -= other.getY();
        z -= other.getZ();
        w -= other.getW();
        return this;
    }

    public double dot(Vector4 other) {
        double sum = 0;
        sum += x * other.getX();
        sum += y * other.getY();
        sum += z * other.getZ();
        sum += w * other.getW();
        return sum;
    }

    @Override
    public String toString() {
        return "Vector4(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
