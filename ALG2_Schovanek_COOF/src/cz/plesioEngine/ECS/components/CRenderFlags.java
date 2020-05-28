package cz.plesioEngine.ECS.components;

import cz.plesioEngine.ECS.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains flags used for special-case rendering.
 *
 * @author plesio
 */
public final class CRenderFlags implements IComponent {

	public int entityID;
	public int componentListIndex = INVALID_COMPONENT_ID;
	public List<CRenderFlagsEnum> flags = new ArrayList<>();

	public CRenderFlags(CRenderFlagsEnum... flags) {
		this.flags.addAll(Arrays.asList(flags));
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
