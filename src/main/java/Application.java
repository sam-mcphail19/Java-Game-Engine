import core.Camera;
import core.Mesh;
import core.Renderer;
import core.Window;
import core.shader.Shader;
import lombok.Getter;
import math.vector.Vector3;
import math.vector.Vector4;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLUtil;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Application {
    private final static long TIME_BETWEEN_FPS_COUNTER_UPDATES_NS = 300_000_000;

    private final String title;
    private final Renderer renderer;
    private final Shader shader;
    private final Window window;
    @Getter
    private final Camera camera;

    private long lastFpsUpdateTime = System.nanoTime();

    public Application(String title) {
        this.title = title;

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window(1280, 720, title, new Vector4(0.2));

        GLFWErrorCallback.createPrint(System.err).set();
        GLUtil.setupDebugMessageCallback();

        renderer = new Renderer();
        shader = new Shader("textured.vsh", "textured.fsh");
        shader.compileShader();
        camera = new Camera(new Vector3(0, 0, 0), 0, 0, window);
    }

    public void update() {
        long startTime = System.nanoTime();

        shader.bind();

        camera.input();

        window.update();
        renderer.render(shader, camera, window);

        shader.unbind();

        long endTime = System.nanoTime();

        // Update the FPS display every 0.3s
        if (endTime - lastFpsUpdateTime > TIME_BETWEEN_FPS_COUNTER_UPDATES_NS) {
            double fps = 1_000_000_000d / (endTime - startTime);
            window.setTitle(this.title + " FPS: " + String.format("%.1f", fps));
            lastFpsUpdateTime = endTime;
        }
    }

    public void exit() {
        window.destroy();

        glfwTerminate();

        GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
        if (errorCallback != null) {
            errorCallback.free();
        }
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window.getHandle());
    }

    public void submitMesh(Mesh mesh) {
        renderer.submit(mesh);
    }
}
