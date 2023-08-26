package core;

import lombok.AllArgsConstructor;
import lombok.Data;
import math.vector.Vector2f;
import math.vector.Vector3f;

@AllArgsConstructor
@Data
public class Vertex {
    public static final int SIZE = 3 + 2;

    private Vector3f pos;
    private Vector2f texCoord;
}
