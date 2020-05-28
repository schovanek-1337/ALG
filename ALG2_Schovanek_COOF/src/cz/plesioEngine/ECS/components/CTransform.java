package cz.plesioEngine.ECS.components;

import cz.plesioEngine.ECS.EntityManager;
import org.joml.Vector3f;

/**
 * A position component.
 *
 * @author plesio
 */
public final class CTransform implements IComponent {

	public int entityID;
	public int componentListIndex;
	public Vector3f position;
	public float rotX, rotY, rotZ;
	public float scale;

	/**
	 * A position component compromising of position data, rotation and
	 * scale
	 *
	 * @param position vec3f position
	 * @param rotX euler x rotation
	 * @param rotY euler y rotation
	 * @param rotZ euler z rotation
	 * @param scale scale
	 */
	public CTransform(Vector3f position, float rotX, float rotY,
		float rotZ, float scale) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		notifyCreation();
	}

	/**
	 * A position component compromising of position data, rotation and
	 * scale
	 *
	 * @param x x pos
	 * @param y y pos
	 * @param z z pos
	 * @param rotX euler x rotation
	 * @param rotY euler y rotation
	 * @param rotZ euler z rotation
	 * @param scale scale
	 */
	public CTransform(float x, float y, float z, float rotX, float rotY,
		float rotZ, float scale) {
		this.position = new Vector3f(x, y, z);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		notifyCreation();
	}

	@Override
	public String toString() {
		return "CTransform{" + "entityID=" + entityID
			+ ", componentListIndex=" + componentListIndex
			+ ", position=" + position
			+ ", rotX=" + rotX
			+ ", rotY=" + rotY
			+ ", rotZ=" + rotZ
			+ ", scale=" + scale + '}';
	}

	@Override
	public void notifyCreation() {
		this.componentListIndex = EntityManager.createComponent(this);
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
