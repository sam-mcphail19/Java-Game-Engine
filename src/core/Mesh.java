package core;

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

    public Mesh(Vertex[] vertices, Integer[] indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.texture = Texture.load("res/placeholder.png");
    }
}
