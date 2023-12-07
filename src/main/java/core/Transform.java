package core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import math.matrix.Mat4;
import math.vector.Vector3;

@Data
@Builder
@AllArgsConstructor
public class Transform {
    @Builder.Default
    private Vector3 translation = new Vector3();
    @Builder.Default
    private Mat4 rotation = Mat4.identity();
    @Builder.Default
    private Vector3 scale = new Vector3(1);

    public Mat4 getModelMat() {
        return Mat4.translate(this.getTranslation())
            .multiply(this.getRotation())
            .multiply(Mat4.scale(this.getScale()));
    }
}
