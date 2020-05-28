package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.entities.PhysicsEntity;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import cz.plesioEngine.toolbox.FuzzyMaths;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public final class HealthPickup extends Entity implements PhysicsEntity {

	private Vector3f velocity = new Vector3f(0, 0, 0);

	private float speedCap = 1f;
	private float decelerationFactor = 0.8f;
	private float acceleration = 130;

	private boolean isDead;

	public HealthPickup(Vector3f position, float rotX, float rotY, float rotZ,
		float scale) {
		super(new TexturedMesh(
			MeshMaster.requestMesh("soap"),
			TextureMaster.requestTexture("soapTexture")),
			position, rotX, rotY, rotZ, scale);
	}

	public void checkPickup() {
		if (FuzzyMaths.compareVectorsFuzzy(getPosition(),
			Camera.getPositionReference(), 20)) {
			if (!isDead && PlayerStats.health.getHealth()
				< PlayerStats.health.getMaxHealth()) {
				PlayerStats.health.changeHealth(15);
				EntityRenderer.removeEntity(this);
				isDead = true;
			}
		}
	}

	@Override
	public void move() {
		accelerate(new Vector3f(0, -1, 0), speedCap, acceleration * 0.2f);
		PhysicsEngine.checkEntityMovement(this, velocity);
		Vector3f position = new Vector3f(getPosition());
		setPosition(position.add(velocity));
		checkPickup();
	}

	@Override
	public boolean getIsDead() {
		return isDead;
	}

	@Override
	public void accelerate(Vector3f wishDirection,
		float wishSpeed, float acceleration) {

		PhysicsEngine.checkEntityMovement(this, wishDirection);

		float currentSpeed = velocity.dot(wishDirection);
		float addspeed = wishSpeed - currentSpeed;

		if (addspeed <= 0) {
			return;
		}

		float accelspeed = acceleration * DisplayManager.getDelta() * wishSpeed;

		if (accelspeed > addspeed) {
			accelspeed = addspeed;
		}

		velocity.x += accelspeed * wishDirection.x;
		velocity.y += accelspeed * wishDirection.y;
		velocity.z += accelspeed * wishDirection.z;
	}

	@Override
	public void decelerate() {
		velocity.mul(decelerationFactor * DisplayManager.getDelta());
	}

	@Override
	public void decelerate(float factor) {
		velocity.mul(factor * DisplayManager.getDelta());
	}
}
