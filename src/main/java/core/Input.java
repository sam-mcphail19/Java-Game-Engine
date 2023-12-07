package core;

import lombok.Getter;
import math.vector.Vector2;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input {
    private static final int MAX_KEYS = 1024;
    private static final int MAX_BUTTONS = 32;

    private static boolean[] keys = new boolean[MAX_KEYS];
    private static boolean[] buttons = new boolean[MAX_BUTTONS];
    @Getter
    private static Vector2 mousePos = new Vector2(0, 0);

    public static boolean isKeyPressed(int keyCode) {
        return keys[keyCode];
    }

    public static boolean isButtonPressed(int button) {
        return buttons[button];
    }

    protected static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }

    protected static void mouseButtonCallback(long window, int button, int action, int mods) {
        buttons[button] = action != GLFW_RELEASE;
    }

    protected static void cursorPosCallback(long window, double xPos, double yPos) {
        mousePos.setX((float) xPos);
        mousePos.setY((float) yPos);
    }
}
