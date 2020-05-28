package cz.plesioEngine.ECS.components;

import cz.plesioEngine.ECS.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Physics flag component
 * @author plesio
 */
public final class CPhysicsFlags implements IComponent {

	public int entityID;
	public int componentListIndex = INVALID_COMPONENT_ID;

	public List<CPhysicsFlags> flags = new ArrayList<>();

	public CPhysicsFlags() {
		notifyCreation();
	}

	@Override
	public int getEntityID() {
		return entityID;
	}

	@Override
	public void setEntityID(int ID) {
		this.entityID = ID;
	}

	@Override
	public void notifyCreation() {
		EntityManager.createComponent(this);
	}

	@Override
	public void setComponentListIndex(int index) {
		this.componentListIndex = index;
	}

}
