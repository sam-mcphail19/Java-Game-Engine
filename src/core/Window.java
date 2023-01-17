package core;

import java.nio.IntBuffer;
import math.vector.Vector2f;
import math.vector.Vector4f;
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
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;


public class Window {
    public final long handle;
    private int width;
    private int height;
    private String title;
    private Vector4f backgroundColor;

    public Window(int width, int height, String title, Vector4f backgroundColor) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.backgroundColor = backgroundColor;

        this.handle = create();
    }

    public void update() {
        if (Input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            GLFW.glfwSetWindowShouldClose(handle, true);
        }
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }

    public void setCursorPos(float x, float y) {
        GLFW.glfwSetCursorPos(handle, x, y);
        Input.cursorPosCallback(handle, x, y);
    }

    public void centerCursor() {
        setCursorPos(width / 2f, height / 2f);
    }

    public void centerCursorHorizontally(float y) {
        setCursorPos(width / 2f, y);
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

    public void setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;

        GL11.glClearColor(
            backgroundColor.getX(),
            backgroundColor.getY(),
            backgroundColor.getX(),
            backgroundColor.getX()
        );
    }

    public void setUseVSync(boolean enabled) {
        GLFW.glfwSwapInterval(enabled ? 1 : 0);
    }

    private long create() {
        GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

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

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                handle,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        GLFW.glfwMakeContextCurrent(handle);
        setUseVSync(false);

        GL.createCapabilities();
        setBackgroundColor(backgroundColor);

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GLFW.glfwShowWindow(handle);
        return handle;
    }
}
