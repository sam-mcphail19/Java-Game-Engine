import graphics.Mesh;
import graphics.Renderer;
import graphics.Texture;
import graphics.Vertex;
import graphics.Window;
import graphics.shader.Shader;
import math.vector.Vector2f;
import math.vector.Vector3f;
import math.vector.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLUtil;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;


public class Application {
    private final String title;
    private Window window;
    private Renderer renderer;
    private Shader shader;

    Vertex[] vertices = new Vertex[]{
        new Vertex(new Vector3f(-0.5f, -0.5f, 0f), new Vector2f(0f, 0f)),
        new Vertex(new Vector3f(-0.5f, 0.5f, 0f), new Vector2f(0f, 1f)),
        new Vertex(new Vector3f(0.5f, 0.5f, 0f), new Vector2f(1f, 1f)),
        new Vertex(new Vector3f(0.5f, -0.5f, 0f), new Vector2f(1f, 0f))
    };
    Integer[] indices = new Integer[]{
        0, 1, 2, 0, 2, 3
    };

    public Application(String title) {
        this.title = title;
    }

    public void start() {
        init();
        loop();
        exit();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        Vector4f backgroundColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        window = new Window(1080, 720, title, backgroundColor);
        renderer = new Renderer();
        shader = new Shader("textured.vsh", "textured.fsh");
        shader.compileShader();

        GLUtil.setupDebugMessageCallback();

        renderer.useWireframe(false);
        renderer.submit(new Mesh(vertices, indices, Texture.load("res/placeholder.png")));
    }

    private void loop() {
        long startTime;
        long endTime;
        long lastFpsUpdateTime = 0;
        while (!glfwWindowShouldClose(window.handle)) {
            startTime = System.nanoTime();

            shader.bind();
            window.update();
            renderer.update();

            endTime = System.nanoTime();

            // Update the FPS display every 0.3s
            if (endTime - lastFpsUpdateTime > 3_000_000_00) {
                double fps = 1_000_000_000d * 1 / (endTime - startTime);
                window.setTitle(this.title + " FPS: " + String.format("%.1f", fps));
                lastFpsUpdateTime = endTime;
            }
        }
    }

    private void exit() {
        window.destroy();

        glfwTerminate();

        GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
        if (errorCallback != null) {
            errorCallback.free();
        }
    }
}
