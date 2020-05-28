package cz.plesioEngine.entities;

import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.entities.staticEntities.EntityCreator;
import cz.plesioEngine.entities.lights.Light;
import cz.plesioEngine.entities.lights.LightMaster;
import cz.plesioEngine.debugConsole.ConsoleInput;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.gameLogic.physicsEngine.AABB;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.DisplayManager;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author plesio
 */
public class Camera {

	private static Vector3f velocity = new Vector3f(0, 0, 0);

	private static Vector3f position = new Vector3f(0, 0, 0);
	private static float pitch = 0;
	private static float yaw = 0;
	private static float roll = 0;

	private static float speedCap = 1f;
	private static float decelerationFactor = 0.80f;
	private static float acceleration = 100;

	private static float freecamSpeed = 0.45f;

	private static Entity boundingBox;

	private static boolean alreadyJumped;

	private static Light playerLight;

	private static boolean playerMoved;

	public static void jump() {
		if (!alreadyJumped) {
			alreadyJumped = true;
			accelerate(new Vector3f(0, 1, 0), speedCap * 3.5f, acceleration);
		}
	}

	private Camera() {
	}

	public static void init() {
		Entity cameraBoundingBox = EntityCreator.createEntity(
			"cameraBoundingBox", "white");

		cameraBoundingBox.setDisableRender(true);
		EntityRenderer.processEntity(cameraBoundingBox);

		cameraBoundingBox.setScale(2f);

		boundingBox = cameraBoundingBox;

		LightMaster.createLight("playerLight", Camera.getPositionInstance(),
			new Vector3f(0.604f, 0.612f, 0.906f), false);

		playerLight = LightMaster.getLight("playerLight");

		PhysicsEngine.processEntity(boundingBox);

		Camera.setPitch(0);
	}

	public static void move() {

		if (!playerMoved) {
			PlayerStats.time = 0;
		}

		if (GameStateMaster.getCurrentGameState() != GameState.PLAYING) {
			return;
		}

		accelerate(new Vector3f(0, -1, 0), speedCap, acceleration * 0.2f);
		PhysicsEngine.checkPlayerMovement(velocity, Camera.getPositionReference());

		if (ConsoleInput.getIsTyping()) {
			return;
		}

		updateLight();

		updateBoundingBoxPosition();

		float turnAngle = (float) Math.toRadians(yaw);

		//KEYS
		{
			if (glfwGetKey(DisplayManager.getWindowHandle(), GLFW_KEY_W)
				== GLFW_PRESS) {

				Vector3f v = new Vector3f();
				v.x += (float) Math.sin(turnAngle);
				v.z -= (float) Math.cos(turnAngle);
				v.normalize();
				accelerate(v, speedCap, acceleration);

				playerMoved = true;

			}
			if (glfwGetKey(DisplayManager.getWindowHandle(), GLFW_KEY_S)
				== GLFW_PRESS) {

				Vector3f v = new Vector3f();
				v.x -= (float) Math.sin(turnAngle);
				v.z += (float) Math.cos(turnAngle);
				v.normalize();
				accelerate(v, speedCap, acceleration);

				playerMoved = true;

			}
			if (glfwGetKey(DisplayManager.getWindowHandle(), GLFW_KEY_A)
				== GLFW_PRESS) {
				Vector3f v = new Vector3f();
				v.x -= (float) Math.cos(turnAngle);
				v.z -= (float) Math.sin(turnAngle);
				v.normalize();
				accelerate(v, speedCap, acceleration);
				tilt(0.1f);
				playerMoved = true;
			}
			if (glfwGetKey(DisplayManager.getWindowHandle(), GLFW_KEY_D)
				== GLFW_PRESS) {
				Vector3f v = new Vector3f();
				v.x += (float) Math.cos(turnAngle);
				v.z += (float) Math.sin(turnAngle);
				v.normalize();
				accelerate(v, speedCap, acceleration);
				tilt(-0.1f);
				playerMoved = true;
			}
		}

		position.add(velocity);
		untilt();

	}

	private static void tilt(float angle) {
		if (Math.abs(roll + angle) < 5) {
			roll += angle;
		}
	}

	private static void untilt() {
		if (roll > 0) {
			roll -= 0.05f;
		}

		if (roll < 0) {
			roll += 0.05f;
		}
	}

	private static void updateLight() {
		if (playerLight == null) {
			return;
		}
		Vector3f lightPosition = new Vector3f(position);
		lightPosition.y += boundingBox.getBoundingBox().getMax()[1].getValue() / 2f;
		playerLight.setPosition(lightPosition);
	}

	public static void accelerate(Vector3f wishDirection,
		float wishSpeed, float acceleration) {

		PhysicsEngine.checkPlayerMovement(wishDirection, Camera.getPositionReference());

		float currentSpeed = velocity.dot(wishDirection);
		float addspeed = wishSpeed - currentSpeed;

		if (addspeed <= 0) {
			return;
		}

		float accelspeed = acceleration * wishSpeed * DisplayManager.getDelta();

		if (accelspeed > addspeed) {
			accelspeed = addspeed;
		}

		velocity.x += accelspeed * wishDirection.x;
		velocity.y += accelspeed * wishDirection.y;
		velocity.z += accelspeed * wishDirection.z;

	}

	public static void decelerate() {
		velocity.mul(decelerationFactor * DisplayManager.getDelta());
	}

	public static void decelerate(float factor) {
		velocity.mul(factor * DisplayManager.getDelta());
	}

	public static void setVelocity(Vector3f v) {
		velocity = v;
	}

	public static Vector3f getDirection() {
		return velocity;
	}

	public static Vector3f getInverseDirection() {
		return new Vector3f(-velocity.x, -velocity.y, -velocity.z);
	}

	private static void updateBoundingBoxPosition() {
		boundingBox.setPosition(Camera.getPositionReference());
	}

	public static Vector3f getPositionInstance() {
		return new Vector3f(position.x, position.y, position.z);
	}

	public static Vector3f getPositionReference() {
		return position;
	}

	public static float getPitch() {
		return pitch;
	}

	public static float getYaw() {
		return yaw;
	}

	public static float getRoll() {
		return roll;
	}

	public static void setPosition(Vector3f newPosition) {
		position = newPosition;
	}

	public static void setPitch(float pitch) {
		if (Camera.pitch + pitch < 180 && Camera.pitch + pitch > -180) {
			Camera.pitch = pitch;
		}

		if (Camera.pitch < -91) {
			Camera.pitch = 0;
		}

		if (Camera.pitch > 91) {
			Camera.pitch = 0;
		}
	}

	public static void setYaw(float yaw) {
		Camera.yaw = yaw;
	}

	public static void setRoll(float roll) {
		Camera.roll = roll;
	}

	public static void setFreecamSpeed(float speed) {
		freecamSpeed = speed;
	}

	public static AABB getBoundingBox() {
		return boundingBox.getBoundingBox();
	}

	public static float getSpeedCap() {
		return speedCap;
	}

	public static float getDecelerationFactor() {
		return decelerationFactor;
	}

	public static float getAccelerate() {
		return acceleration;
	}

	public static boolean isAlreadyJumped() {
		return alreadyJumped;
	}

	public static void setAlreadyJumped(boolean alreadyJumped) {
		Camera.alreadyJumped = alreadyJumped;
	}

}
