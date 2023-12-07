package core;

import java.nio.IntBuffer;
import lombok.Getter;
import math.vector.Vector4;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class Window {
    private static final long MIN_TIME_BETWEEN_TOGGLES_MS = 100L;

    @Getter
    private final long handle;
    private int width;
    private int height;
    private String title;
    private Vector4 backgroundColor;
    private boolean useWireFrame = false;
    private long lastToggleMs = System.currentTimeMillis();

    public Window(int width, int height, String title, Vector4 backgroundColor) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.backgroundColor = backgroundColor;

        this.handle = create();
    }

    public void update() {
        if (Input.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            GLFW.glfwSetWindowShouldClose(handle, true);
        }
        if (Input.isKeyPressed(GLFW.GLFW_KEY_Z) && System.currentTimeMillis() - lastToggleMs > MIN_TIME_BETWEEN_TOGGLES_MS) {
            lastToggleMs = System.currentTimeMillis();
            toggleUseWireframe();
        }
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }

    public void setCursorPos(double x, double y) {
        GLFW.glfwSetCursorPos(handle, x, y);
        Input.cursorPosCallback(handle, x, y);
    }

    public void centerCursor() {
        setCursorPos(width / 2d, height / 2d);
    }

    public void centerCursorHorizontally(double y) {
        setCursorPos(width / 2d, y);
    }

    public void hideCursor() {
        GLFW.glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void showCursor() {
        GLFW.glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public void setTitle(String title) {
        this.title = title;
        GLFW.glfwSetWindowTitle(handle, title);
    }

    public void setBackgroundColor(Vector4 backgroundColor) {
        this.backgroundColor = backgroundColor;

        GL11.glClearColor(
            (float) backgroundColor.getX(),
            (float) backgroundColor.getY(),
            (float) backgroundColor.getX(),
            (float) backgroundColor.getX()
        );
    }

    public void setUseVSync(boolean enabled) {
        GLFW.glfwSwapInterval(enabled ? 1 : 0);
    }

    public void toggleUseWireframe() {
        useWireFrame = !useWireFrame;
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, useWireFrame ? GL11.GL_LINE : GL11.GL_FILL);
    }

    private long create() {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);

        long handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetKeyCallback(handle, Input::keyCallback);
        GLFW.glfwSetMouseButtonCallback(handle, Input::mouseButtonCallback);
        GLFW.glfwSetCursorPosCallback(handle, Input::cursorPosCallback);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                handle,
                (vidMode.width() - pWidth.get(0)) / 2,
                (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        GLFW.glfwMakeContextCurrent(handle);
        setUseVSync(false);

        GL.createCapabilities();
        setBackgroundColor(backgroundColor);

        GLFW.glfwShowWindow(handle);
        return handle;
    }
}
