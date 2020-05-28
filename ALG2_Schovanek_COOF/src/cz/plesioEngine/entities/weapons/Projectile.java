package cz.plesioEngine.entities.weapons;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Projectile extends Entity {

	private Vector3f projectileVector;

	private float projectileOffsetDistance;

	private float projectileSpeed = 1f;
	private float projectileLifespan = 1f;

	private WeaponEntity firedFrom = null;

	public Projectile(TexturedMesh texturedMesh, Vector3f position,
		float rotX, float rotY, float rotZ, float scale) {
		super(texturedMesh, position, rotX, rotY, rotZ, scale);
	}

	public Vector3f getProjectileVector() {
		return projectileVector;
	}

	public void setProjectileVector(Vector3f projectileVector) {
		this.projectileVector = projectileVector;
	}

	public Projectile getModelInstance() {
		return new Projectile(this.getTexturedModel(), this.getPosition(),
			this.getRotX(), this.getRotY(), this.getRotZ(), this.getScale());
	}

	public float getProjectileSpeed() {
		return projectileSpeed;
	}

	public void setProjectileSpeed(float projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}

	/**
	 * Calculates the offset from screen at which the projectiles are fired
	 * from.
	 *
	 * @return
	 */
	public Vector3f getProjectileOffset() {

		Vector3f finalPos = new Vector3f();

		Vector3f cameraPos = Camera.getPositionInstance();

		float horizDistance = (float) (projectileOffsetDistance * Math.cos(Math.toRadians(Camera.getPitch())));

		float verticalDistance = (float) (projectileOffsetDistance * Math.sin(Math.toRadians(Camera.getPitch())));

		float theta = 180 - Camera.getYaw();
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

		finalPos.x = cameraPos.x() - offsetX;
		finalPos.z = cameraPos.z() - offsetZ;
		finalPos.y = cameraPos.y() + verticalDistance;

		return finalPos;
	}

	public void setProjectileOffset(float offset) {
		this.projectileOffsetDistance = -offset;
	}

	public float getProjectileLifespan() {
		return projectileLifespan;
	}

	public void setProjectileLifespan(float projectileLifespan) {
		this.projectileLifespan = projectileLifespan;
	}

	public WeaponEntity getFiredFrom() {
		return firedFrom;
	}

	public void setFiredFrom(WeaponEntity firedFrom) {
		this.firedFrom = firedFrom;
	}

}
