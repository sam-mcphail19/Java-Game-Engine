package core;

import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import util.BufferUtil;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class Renderer {
    private static final long MIN_TIME_BETWEEN_WIREFRAME_TOGGLES_MS = 200;

    private int vao;
    private int ibo;
    private int vbo;
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Integer> indices = new ArrayList<>();
    private boolean useWireframe = false;
    private long lastWireframeToggleTime = System.currentTimeMillis();

    public Renderer() {
        useWireframe(false);

        vao = BufferUtil.createVao();
        bindVao();

        vbo = BufferUtil.createBuffer();
        ibo = BufferUtil.createBuffer();
        bindVbo();

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, Vertex.SIZE * 4, 0);
        GL20.glVertexAttribPointer(1, 2, GL20.GL_FLOAT, false, Vertex.SIZE * 4, 3 * 4);

        unbindVbo();
        unbindVao();
    }

    public void update() {
        clear();

        input();

        bindVbo();
        BufferUtil.bufferVboData(BufferUtil.createFloatBuffer(vertices));

        bindVao();
        bindIbo();
        BufferUtil.bufferIboData(BufferUtil.createIntBuffer(indices));

        draw();

        unbindVbo();
        unbindIbo();
        unbindVao();
    }

    public void submit(Mesh mesh) {
        vertices.addAll(Arrays.asList(mesh.getVertices()));
        indices.addAll(Arrays.asList(mesh.getIndices()));
    }

    public void useWireframe(boolean enabled) {
        useWireframe = enabled;
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, enabled ? GL11.GL_LINE : GL11.GL_FILL);
    }

    public void toggleUseWireframe() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastWireframeToggleTime < MIN_TIME_BETWEEN_WIREFRAME_TOGGLES_MS) {
            return;
        }
        lastWireframeToggleTime = currentTime;
        useWireframe(!this.useWireframe);
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void input() {
        if (Input.isKeyPressed(GLFW_KEY_Z)) {
            toggleUseWireframe();
        }
    }

    private void draw() {
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.size(), GL11.GL_UNSIGNED_INT, 0);
    }

    private void bindVbo() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
    }

    private void bindVao() {
        GL30.glBindVertexArray(vao);
    }

    private void bindIbo() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
    }

    private void unbindVbo() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindIbo() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void unbindVao() {
        GL30.glBindVertexArray(0);
    }
}
