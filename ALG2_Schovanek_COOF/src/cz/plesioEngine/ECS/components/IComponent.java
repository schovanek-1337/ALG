package cz.plesioEngine.ECS.components;

/**
 * Entity component interface.
 *
 * @author plesio
 */
public interface IComponent {

	/**
	 * Every component's list ID is set to this on creation.
	 */
	int INVALID_COMPONENT_ID = -1;

	/**
	 * Returns the entity ID of the current component.
	 * @return entity ID
	 */
	public int getEntityID();

	/**
	 * Changes the entity ID of the current component.
	 * @param ID ID of the entity.
	 */
	public void setEntityID(int ID);

	/**
	 * Notify the entity manager of a components creation and generates 
	 * a new ID for this component.
	 */
	public void notifyCreation();

	/**
	 * Call on creation with conjunction with notifyCreation()
	 * @param index 
	 */
	public void setComponentListIndex(int index);

}
