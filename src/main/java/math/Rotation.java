package math;

import lombok.AllArgsConstructor;
import lombok.Data;
import math.vector.Vector3f;

@Data
@AllArgsConstructor
public class Rotation {
    private float angleDegrees;
    private Vector3f axis;
}
