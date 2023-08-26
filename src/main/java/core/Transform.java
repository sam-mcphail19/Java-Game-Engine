package core;

import lombok.Data;
import math.Rotation;
import math.vector.Vector3f;

@Data
public class Transform {
    private Vector3f translation;
    private Rotation rotation;
    private Vector3f scale;
}
