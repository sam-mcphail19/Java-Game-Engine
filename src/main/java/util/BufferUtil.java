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

    public static float[] createFloatBuffer(List<Vertex> vertices) {
        float[] buffer = new float[vertices.size() * Vertex.SIZE];

        for (int i = 0; i < vertices.size(); i++) {
            buffer[i * Vertex.SIZE] = (float) vertices.get(i).getPos().getX();
            buffer[i * Vertex.SIZE + 1] = (float) vertices.get(i).getPos().getY();
            buffer[i * Vertex.SIZE + 2] = (float) vertices.get(i).getPos().getZ();
            buffer[i * Vertex.SIZE + 3] = (float) vertices.get(i).getTexCoord().getX();
            buffer[i * Vertex.SIZE + 4] = (float) vertices.get(i).getTexCoord().getY();
        }

        return buffer;
    }

    public static void bufferVboData(float[] buffer) {
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public static void bufferIboData(int[] buffer) {
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
}
