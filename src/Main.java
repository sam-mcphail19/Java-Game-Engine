import graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import math.vector.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;


public class Main {

    private final static String windowTitle = "Java Game Engine";
    private static Window window;

    public static void main(String[] args) {
        init();
        loop();
        exit();
    }

    private static void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        Vector4f backgroundColor = new Vector4f(0.6f, 0.3f, 0.8f, 1.0f);
        window = new Window(1080, 720, windowTitle, backgroundColor);
    }

    private static void loop() {
        long startTime;
        long endTime;
        long lastFpsUpdateTime = 0;
        while (!glfwWindowShouldClose(window.handle)) {
            startTime = System.nanoTime();
            window.update();
            endTime = System.nanoTime();

            // Update the FPS display every 0.3s
            if (endTime - lastFpsUpdateTime > 3_000_000_00) {
                double fps = 1_000_000_000d * 1 / (endTime - startTime);
                window.setTitle(windowTitle + " FPS: " + String.format("%.1f", fps));
                lastFpsUpdateTime = endTime;
            }
        }
    }

    private static void exit() {
        window.destroy();

        glfwTerminate();

        GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
        if (errorCallback != null) {
            errorCallback.free();
        }
    }
}