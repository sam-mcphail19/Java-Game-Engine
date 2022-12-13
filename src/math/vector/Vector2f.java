package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Vector2f {

    private float x;
    private float y;

    public Vector2f add(Vector2f other) {
        x += other.getX();
        y += other.getY();
        return this;
    }

    public Vector2f subtract(Vector2f other) {
        x -= other.getX();
        y -= other.getY();
        return this;
    }

    public float dot(Vector2f other) {
        float sum = 0;
        sum += x * other.getX();
        sum += y * other.getY();
        return sum;
    }

    @Override
    public String toString() {
        return "Vector2f(" + x + ", " + y + ")";
    }
}
