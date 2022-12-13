package graphics.shader;

import lombok.Getter;
import org.lwjgl.opengl.GL20;


public enum ShaderType {
    VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
    FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER);

    @Getter
    private final int glId;

    ShaderType(int id) {
        this.glId = id;
    }
}
