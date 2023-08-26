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
    private final int vao;
    private final int ibo;
    private final int vbo;
    private final List<Vertex> vertices;
    private final List<Integer> indices;
    private final Texture texture;
    private final Transform transform;

    public Mesh(List<Vertex> vertices, List<Integer> indices, Transform transform, Texture texture) {
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

        GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, Vertex.SIZE * 4, 0);
        GL20.glVertexAttribPointer(1, 2, GL20.GL_FLOAT, false, Vertex.SIZE * 4, 3 * 4);

        unbindVbo();
        unbindVao();
    }

    public Mesh(List<Vertex> vertices, List<Integer> indices, Transform transform) {
        this(vertices, indices, transform, Texture.load("src/main/resources/placeholder.png"));
    }

    public void render() {
        bindVbo();
        BufferUtil.bufferVboData(BufferUtil.createFloatBuffer(vertices));

        texture.bind();

        bindVao();
        bindIbo();
        BufferUtil.bufferIboData(BufferUtil.createIntBuffer(indices));

        draw();

        texture.unbind();
        unbindVbo();
        unbindIbo();
        unbindVao();
    }

    private void draw() {
        applyTransformation();
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.size(), GL11.GL_UNSIGNED_INT, 0);
    }

    private void applyTransformation() {
        //glTranslate()
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
