package core;

import lombok.Getter;
import math.matrix.Mat4f;
import math.vector.Vector2f;
import math.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;


public class Camera {
    private static final float MOUSE_SENSITIVITY = 0.5f;
    private static final float MOVEMENT_SPEED = 0.0001f;

    @Getter
    private Vector3f pos;
    private float pitch;
    private float yaw;
    private Vector3f viewDir;
    private Window window;
    private Vector2f mousePos;

    public Camera(Vector3f pos, float pitch, float yaw, Window window) {
        this.pos = pos;
        this.pitch = pitch;
        this.yaw = yaw;
        this.window = window;

        window.centerCursor();
        window.hideCursor();

        this.mousePos = Input.getMousePos();
    }

    public void input() {
        float oldY = mousePos.getY();
        float oldX = mousePos.getX();
        mousePos = Input.getMousePos().copy();

        float pitch = (mousePos.getY() - oldY) * MOUSE_SENSITIVITY;
        pitch = pitch < 90f && pitch > -90f ? pitch : 0;

        float yaw = (mousePos.getX() - oldX) * MOUSE_SENSITIVITY;

        addPitch(pitch);
        addYaw(yaw);

        viewDir = calculateViewDirection();
        Vector3f strafeDir = calculateStrafeDirection();

        Vector3f movement = new Vector3f(0f);
        if (Input.isKeyPressed(GLFW_KEY_W)) {
            movement.add(viewDir);
        }
        if (Input.isKeyPressed(GLFW_KEY_A)) {
            movement.add(strafeDir.copy().multiply(-1));
        }
        if (Input.isKeyPressed(GLFW_KEY_S)) {
            movement.add(viewDir.copy().multiply(-1));
        }
        if (Input.isKeyPressed(GLFW_KEY_D)) {
            movement.add(strafeDir);
        }
        if (Input.isKeyPressed(GLFW_KEY_SPACE)) {
            movement.add(Vector3f.yAxis());
        }
        if (Input.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            movement.add(Vector3f.yAxis().multiply(-1));
        }

        move(movement.multiply(MOVEMENT_SPEED));
    }

    public Mat4f viewMatrix() {
        Mat4f viewMat = Mat4f.identity();

        viewMat.multiply(Mat4f.rotate(-pitch, Vector3f.xAxis()));
        viewMat.multiply(Mat4f.rotate(-yaw, Vector3f.yAxis()));
        viewMat.multiply(Mat4f.translate(pos.copy().multiply(-1)));

        return viewMat;
    }

    public void move(Vector3f vec) {
        pos.add(vec);
    }

    public void addPitch(float pitch) {
        this.pitch += pitch;

        this.pitch = Math.max(this.pitch, -90f);
        this.pitch = Math.min(this.pitch, 90f);
    }

    public void addYaw(float yaw) {
        this.yaw += yaw;

        if (this.yaw > 360 || this.yaw < -360) {
            this.yaw = 0;
            window.centerCursorHorizontally(mousePos.getY());
        }
    }

    private Vector3f calculateViewDirection() {
        return new Vector3f(
            (float) Math.sin(Math.toRadians(yaw)),
            0,
            (float) -Math.cos(Math.toRadians(yaw)) // inverted because yaw=0 points towards -z
        );
    }

    private Vector3f calculateStrafeDirection() {
        return viewDir.cross(Vector3f.yAxis()).normalize();
    }

    @Override
    public String toString() {
        return "Camera(pos=" + pos + ", direction=" + viewDir + ")";
    }
}
