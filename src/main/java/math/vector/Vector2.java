package math.vector;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Vector2 {

    private double x;
    private double y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2 add(Vector2 other) {
        x += other.getX();
        y += other.getY();
        return this;
    }

    public Vector2 subtract(Vector2 other) {
        x -= other.getX();
        y -= other.getY();
        return this;
    }

    public double dot(Vector2 other) {
        double sum = 0;
        sum += x * other.getX();
        sum += y * other.getY();
        return sum;
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }
}
