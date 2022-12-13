package graphics;

import lombok.Getter;


@Getter
public class Mesh {
    private Vertex[] vertices;
    private Integer[] indices;
    private Texture texture;

    public Mesh(Vertex[] vertices, Integer[] indices, Texture texture) {
        this.vertices = vertices;
        this.indices = indices;
        this.texture = texture;
    }

    /*
    Set texture to placeholder texture
    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.texture = texture;

        initBuffers();
    }
    */
}
