package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {}

	public void move() {
		
		float moveSpeed = 0.02f;
		float lookSpeed = 0.5f;
		float sprintMultiplier = 1.5f;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			moveSpeed *= sprintMultiplier;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= moveSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += moveSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += moveSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= moveSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += moveSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= moveSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			yaw -= lookSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			yaw += lookSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pitch += lookSpeed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			pitch -= lookSpeed;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
	
}
