package util;

import core.Vertex;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;


public class BufferUtil {

    public static int createVao() {
        return GL30.glGenVertexArrays();
    }

    public static int createBuffer() {
        return GL15.glGenBuffers();
    }

    public static FloatBuffer createFloatBuffer(List<Vertex> vertices) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size() * Vertex.SIZE);
        for (Vertex vertex : vertices) {
            buffer.put(vertex.getPos().getX());
            buffer.put(vertex.getPos().getY());
            buffer.put(vertex.getPos().getZ());
            buffer.put(vertex.getTexCoord().getX());
            buffer.put(vertex.getTexCoord().getY());
        }

        return buffer.flip();
    }

    public static IntBuffer createIntBuffer(List<Integer> ints) {
        return MemoryUtil.memAllocInt(ints.size())
            .put(ints.stream()
                .mapToInt(i -> i)
                .toArray()
            ).flip();
    }

    public static void bufferVboData(FloatBuffer buffer) {
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public static void bufferIboData(IntBuffer buffer) {
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
}
