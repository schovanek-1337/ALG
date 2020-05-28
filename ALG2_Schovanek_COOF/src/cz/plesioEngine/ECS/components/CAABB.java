package cz.plesioEngine.ECS.components;

import cz.plesioEngine.ECS.EntityManager;
import cz.plesioEngine.ECS.systemComponents.EndPoint;

/**
 * Represents an axis-aligned bounding box used for physics calculations.
 *
 * @author plesio
 */
public final class CAABB implements IComponent {

	public int entityID;
	public int componentListIndex = INVALID_COMPONENT_ID;
	public EndPoint[] min = new EndPoint[3];
	public EndPoint[] max = new EndPoint[3];

	public CAABB() {
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
