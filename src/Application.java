import core.Camera;
import core.Mesh;
import core.Renderer;
import core.Vertex;
import core.Window;
import core.shader.Shader;
import math.matrix.Mat4f;
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

    Vertex[] vertices = new Vertex[]{
        new Vertex(new Vector3f(-0.5f, -0.5f, 0f), new Vector2f(0f, 0f)),
        new Vertex(new Vector3f(-0.5f, 0.5f, 0f), new Vector2f(0f, 1f)),
        new Vertex(new Vector3f(0.5f, 0.5f, 0f), new Vector2f(1f, 1f)),
        new Vertex(new Vector3f(0.5f, -0.5f, 0f), new Vector2f(1f, 0f))
    };
    Integer[] indices = new Integer[]{
        0, 1, 2, 0, 2, 3
    };

    private Window window;
    private Renderer renderer;
    private Shader shader;
    private Camera camera;

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

        window = new Window(1280, 720, title, new Vector4f(0.2f));
        renderer = new Renderer();
        shader = new Shader("textured.vsh", "textured.fsh");
        shader.compileShader();
        camera = new Camera(new Vector3f(0, 0, 1f), 0, 0, window);

        GLUtil.setupDebugMessageCallback();

        renderer.useWireframe(false);
        renderer.submit(new Mesh(vertices, indices));
    }

    private void loop() {
        long startTime;
        long endTime;
        long lastFpsUpdateTime = 0;
        float angle = 0;
        float translation = 0;
        while (!glfwWindowShouldClose(window.handle)) {
            startTime = System.nanoTime();

            shader.bind();

            camera.input();

            // MVP = P * V * M;
            // Model - model space to world space
            // View - world space to camera space (i.e. all vertices defined relative to camera)
            // Projection - camera space to homogenous space (i.e. map camera frustum into a cube)

            Mat4f projMat = Mat4f.projection(70f, 16f / 9f, 0.1f, 1000f);
            Mat4f viewMat = camera.viewMatrix();
            Mat4f modelMat = Mat4f.identity();

            //modelMat.multiply(Mat4f.translate(new Vector3f(translation, 0f, 0f)));
            //modelMat.multiply(Mat4f.rotate(angle, Vector3f.zAxis()));
            modelMat.multiply(Mat4f.rotate(angle, Vector3f.yAxis()));
            //modelMat.multiply(Mat4f.rotate(angle, Vector3f.xAxis()));

            Mat4f mvp = projMat.multiply(viewMat.multiply(modelMat));

            shader.setUniformMat4("mvp", mvp);

            window.update();
            renderer.update();

            shader.unbind();

            angle += 0.01;
            angle = angle > 360 ? angle - 360 : angle;

            translation += 0.0001;
            translation = translation > 1.75f ? -1.75f : translation;

            endTime = System.nanoTime();

            // Update the FPS display every 0.3s
            if (endTime - lastFpsUpdateTime > 300_000_000) {
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
