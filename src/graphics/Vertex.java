package graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import math.vector.Vector2f;
import math.vector.Vector3f;


@AllArgsConstructor
public class Vertex {
    public static final int SIZE = 3 + 2;

    @Setter
    @Getter
    private Vector3f pos;
    @Setter
    @Getter
    private Vector2f texCoord;
}
