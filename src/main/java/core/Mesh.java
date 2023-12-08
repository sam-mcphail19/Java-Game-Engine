package core;

import java.util.List;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import util.BufferUtil;

@Getter
public class Mesh {
    private static final int FLOAT_SIZE_IN_BYTES = 4;
    private static final int POS_SIZE = 3;
    private static final int UV_SIZE = 2;

    private final int vao;
    private final int ibo;
    private final int vbo;
    private final List<Vertex> vertices;
    private final int[] indices;
    private final Texture texture;
    private final Transform transform;

    public Mesh(List<Vertex> vertices, int[] indices, Transform transform, Texture texture) {
        this.vertices = vertices;
        this.indices = indices;
        this.transform = transform;
        this.texture = texture;

        vao = BufferUtil.createVao();
        bindVao();

        vbo = BufferUtil.createBuffer();
        ibo = BufferUtil.createBuffer();
        bindVbo();

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // Position (x, y, z)
        GL20.glVertexAttribPointer(0, POS_SIZE, GL20.GL_FLOAT, false, Vertex.SIZE * FLOAT_SIZE_IN_BYTES, 0);
        // UVs (u, v)
        GL20.glVertexAttribPointer(1, UV_SIZE, GL20.GL_FLOAT, false, Vertex.SIZE * FLOAT_SIZE_IN_BYTES, POS_SIZE * FLOAT_SIZE_IN_BYTES);

        unbindVbo();
        unbindVao();

        updateBuffers();
    }

    public void render() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        bindVbo();

        texture.bind();

        bindVao();
        bindIbo();

        draw();

        texture.unbind();
        unbindVbo();
        unbindIbo();
        unbindVao();
    }

    private void draw() {
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
    }

    private void updateBuffers() {
        bindVbo();
        BufferUtil.bufferVboData(BufferUtil.createFloatBuffer(vertices));

        texture.bind();

        bindVao();
        bindIbo();
        BufferUtil.bufferIboData(indices);

        texture.unbind();
        unbindVbo();
        unbindIbo();
        unbindVao();
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
