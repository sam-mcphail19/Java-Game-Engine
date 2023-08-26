package core;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class Renderer {
    private final List<Mesh> meshes;

    public Renderer() {
        meshes = new ArrayList<>();
    }

    public void render() {
        clear();

        meshes.forEach(Mesh::render);
    }

    public void submit(Mesh mesh) {
        meshes.add(mesh);
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
