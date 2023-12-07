package core.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import math.matrix.Mat4;
import org.lwjgl.opengl.GL20;


public class Shader {
    private static final String SHADER_PATH = "src/main/resources/shader/";
    public static final String MVP_UNIFORM = "mvp";
    public static final String MODEL_UNIFORM = "model";

    private final int program;

    public Shader(String vertexShader, String fragmentShader) {
        program = GL20.glCreateProgram();

        if (program == 0) {
            throw new RuntimeException("Shader creation failed: Could not find valid memory location in constructor");
        }

        addProgram(load(vertexShader), ShaderType.VERTEX_SHADER);
        addProgram(load(fragmentShader), ShaderType.FRAGMENT_SHADER);
    }

    public static String load(String fileName) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader shaderReader = new BufferedReader(new FileReader(SHADER_PATH + fileName));
            shaderReader.lines().forEach(line -> shaderSource.append(line).append("\n"));
            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shaderSource.toString();
    }

    public void bind() {
        GL20.glUseProgram(program);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void compileShader() {
        GL20.glLinkProgram(program);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException(GL20.glGetProgramInfoLog(program, 1024));
        }

        GL20.glValidateProgram(program);

        if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException(GL20.glGetProgramInfoLog(program, 1024));
        }
    }

    public void setUniformMat4(String name, Mat4 mat) {
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, mat.getElementsAsF());
    }

    private int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(program, name);
    }

    private void addProgram(String text, ShaderType type) {
        int shader = GL20.glCreateShader(type.getGlId());

        if (shader == 0) {
            throw new RuntimeException("Shader creation failed: Could not find valid memory location when adding shader");
        }

        GL20.glShaderSource(shader, text);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(shader, 1024));
        }

        GL20.glAttachShader(program, shader);
    }
}