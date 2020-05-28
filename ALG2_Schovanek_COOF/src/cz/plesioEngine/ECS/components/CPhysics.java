package cz.plesioEngine.ECS.components;

import cz.plesioEngine.ECS.EntityManager;
import org.joml.Vector3f;

/**
 * A physics component
 * @author plesio
 */
public final class CPhysics implements IComponent {

	public int entityID;
	public int componentListIndex = INVALID_COMPONENT_ID;

	public Vector3f velocity = new Vector3f();
	public float speedCap;
	public float acceleration;
	public float friction;

	public CPhysics(float speedCap, float acceleration, float friction) {
		this.speedCap = speedCap;
		this.acceleration = acceleration;
		this.friction = friction;
		notifyCreation();
	}

	@Override
	public void notifyCreation() {
		EntityManager.createComponent(this);
	}

	@Override
	public void setEntityID(int ID) {
		this.entityID = ID;
	}

	@Override
	public int getEntityID() {
		return entityID;
	}

	@Override
	public void setComponentListIndex(int index) {
		componentListIndex = index;
	}

}
