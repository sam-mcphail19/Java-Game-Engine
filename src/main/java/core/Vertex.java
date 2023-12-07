package core;

import lombok.AllArgsConstructor;
import lombok.Data;
import math.vector.Vector2;
import math.vector.Vector3;

@AllArgsConstructor
@Data
public class Vertex {
    // X, Y, Z, U, V
    public static final int SIZE = 3 + 2;

    private Vector3 pos;
    private Vector2 texCoord;
}
