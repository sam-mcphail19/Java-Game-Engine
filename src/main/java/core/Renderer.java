package core;

import core.shader.Shader;
import java.util.ArrayList;
import java.util.List;
import math.matrix.Mat4;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Renderer {
    private final List<Mesh> meshes;
    private static final Mat4 PROJ_MAT = Mat4.projection(70, 16 / 9d, 0.5, 100);

    public Renderer() {
        meshes = new ArrayList<>();
    }

    public void render(Shader shader, Camera camera, Window window) {
        clear();

        Mat4 viewMat = camera.viewMatrix();

        for (Mesh mesh : meshes) {
            Mat4 mvp = PROJ_MAT.multiply(viewMat).multiply(mesh.getTransform().getModelMat());
            shader.setUniformMat4(Shader.MVP_UNIFORM, mvp);

            mesh.render();
        }

        GLFW.glfwSwapBuffers(window.getHandle());
    }

    public void submit(Mesh mesh) {
        meshes.add(mesh);
    }

    private void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
