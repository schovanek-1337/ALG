package cz.plesioEngine.entities;

import cz.plesioEngine.gameLogic.physicsEngine.AABB;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public interface PhysicsEntity {
	
	public void accelerate(Vector3f wishDirection, float wishSpeed, float acceleration);
	public void decelerate();
	public void decelerate(float amount);
	public void move();
	public boolean getIsDead();
	public AABB getBoundingBox();
	
}
