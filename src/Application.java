import core.Camera;
import core.Mesh;
import core.Renderer;
import core.Window;
import core.shader.Shader;
import math.matrix.Mat4f;
import math.vector.Vector3f;
import math.vector.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;

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
    private final Camera camera;

    private long lastFpsUpdateTime = System.nanoTime();

    public Application(String title) {
        this.title = title;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window(1280, 720, title, new Vector4f(0.2f));
        renderer = new Renderer();
        shader = new Shader("textured.vsh", "textured.fsh");
        shader.compileShader();
        camera = new Camera(new Vector3f(0, 0, 1f), 0, 0, window);
    }

    public void update() {
        long startTime = System.nanoTime();

        shader.bind();

        camera.input();

        Mat4f projMat = Mat4f.projection(70f, 16f / 9f, 0.1f, 1000f);
        Mat4f viewMat = camera.viewMatrix();
        Mat4f modelMat = Mat4f.identity();

        Mat4f mvp = projMat.multiply(viewMat.multiply(modelMat));

        shader.setUniformMat4(Shader.MVP_UNIFORM, mvp);

        window.update();
        renderer.update();

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
        return glfwWindowShouldClose(window.handle);
    }

    public void useWireframe(boolean useWireframe) {
        renderer.useWireframe(useWireframe);
    }

    public void submitMesh(Mesh mesh) {
        renderer.submit(mesh);
    }
}
