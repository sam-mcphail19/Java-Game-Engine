package core;

import lombok.Getter;
import math.matrix.Mat4;
import math.vector.Vector2;
import math.vector.Vector3;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class Camera {
    private static final double MOUSE_SENSITIVITY = 0.24;
    private static final double MOVEMENT_SPEED = 0.000000005;

    @Getter
    private Vector3 pos;
    private double pitch;
    private double yaw;
    private Vector3 viewDir;
    private Window window;
    private Vector2 mousePos;

    private long lastUpdateTime = 0;

    public Camera(Vector3 pos, double pitch, double yaw, Window window) {
        this.pos = pos;
        this.pitch = pitch;
        this.yaw = yaw;
        this.window = window;

        window.centerCursor();
        window.hideCursor();

        this.mousePos = Input.getMousePos();
    }

    public void input() {
        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        double oldY = mousePos.getY();
        double oldX = mousePos.getX();
        mousePos = Input.getMousePos().copy();

        double pitch = (mousePos.getY() - oldY) * MOUSE_SENSITIVITY;
        pitch = pitch < 90f && pitch > -90f ? pitch : 0;

        double yaw = (mousePos.getX() - oldX) * MOUSE_SENSITIVITY;

        addPitch(pitch);
        addYaw(yaw);

        viewDir = calculateViewDirection();
        Vector3 strafeDir = calculateStrafeDirection();

        Vector3 movement = new Vector3();
        if (Input.isKeyPressed(GLFW_KEY_W)) {
            movement = movement.add(viewDir);
        }
        if (Input.isKeyPressed(GLFW_KEY_A)) {
            movement = movement.add(strafeDir.multiply(-1));
        }
        if (Input.isKeyPressed(GLFW_KEY_S)) {
            movement = movement.add(viewDir.multiply(-1));
        }
        if (Input.isKeyPressed(GLFW_KEY_D)) {
            movement = movement.add(strafeDir);
        }
        if (Input.isKeyPressed(GLFW_KEY_SPACE)) {
            movement = movement.add(Vector3.yAxis());
        }
        if (Input.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            movement = movement.add(Vector3.yAxis().multiply(-1));
        }

        move(movement.multiply(MOVEMENT_SPEED * deltaTime));
    }

    public Mat4 viewMatrix() {
        return Mat4.xRotation(pitch)
            .multiply(Mat4.yRotation(yaw))
            .multiply(Mat4.translate(pos.copy().multiply(-1)));
    }

    public void move(Vector3 vec) {
        pos = pos.add(vec);
    }

    public void addPitch(double pitch) {
        this.pitch += pitch;

        this.pitch = Math.max(this.pitch, -90);
        this.pitch = Math.min(this.pitch, 90);
    }

    public void addYaw(double yaw) {
        this.yaw += yaw;

        if (this.yaw > 360 || this.yaw < -360) {
            this.yaw = 0;
            window.centerCursorHorizontally(mousePos.getY());
        }
    }

    private Vector3 calculateViewDirection() {
        return new Vector3(
            Math.sin(Math.toRadians(yaw)),
            0,
            -Math.cos(Math.toRadians(yaw)) // inverted because yaw=0 points towards -z
        );
    }

    private Vector3 calculateStrafeDirection() {
        return viewDir.cross(Vector3.yAxis()).normalize();
    }

    @Override
    public String toString() {
        return "Camera(pos=" + pos + ", direction=" + viewDir + ")";
    }
}
