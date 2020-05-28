package cz.plesioEngine.ECS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cz.plesioEngine.ECS.components.IComponent;

/**
 * Entity variant using ECS
 *
 * @author plesio
 */
public class Entity {

	private int ID;

	private boolean canEverTick = true;

	private List<IComponent> components = new ArrayList<>();

	/**
	 * Creates an entity with a desired ID
	 *
	 * @param ID
	 */
	protected Entity(int ID) {
		this.ID = ID;
	}

	/**
	 * Creates an entity with a desired ID, and a specified canEverTick
	 * parameter
	 *
	 * @param ID desired entity ID
	 * @param canEverTick if false, then the entity won't be updated
	 */
	protected Entity(int ID, boolean canEverTick) {
		this.ID = ID;
		this.canEverTick = canEverTick;
	}

	/**
	 * Add components to an entity
	 *
	 * @param components
	 */
	public void addComponents(IComponent... components) {
		for (IComponent c : components) {
			c.setEntityID(ID);
		}
		this.components.addAll(Arrays.asList(components));
	}

	/**
	 * Get a component from an entity given its class type
	 *
	 * @param <T>
	 * @param cType
	 * @return
	 */
	public <T extends IComponent> List<T> getComponent(Class cType) {
		List<T> returnComponents = new ArrayList<>();
		for (IComponent c : components) {
			if (c.getClass().equals(cType)) {
				returnComponents.add((T) c);
			}
		}
		return (returnComponents.isEmpty()) ? null
			: returnComponents;
	}

	/**
	 * Remove components from an entity
	 *
	 * @param cTypes
	 */
	public void removeComponents(Class... cTypes) {
		List<IComponent> batchToRemove = new ArrayList<>();
		for (Class c : cTypes) {
			for (IComponent component : components) {
				if (component.getClass().equals(c)) {
					batchToRemove.add(component);
				}
			}
		}
		components.removeAll(batchToRemove);
	}

	/**
	 * Renders the entity immutable
	 *
	 * @param canEverTick
	 */
	public void setCanEverTick(boolean canEverTick) {
		this.canEverTick = canEverTick;
	}

	/**
	 * Can the entity update
	 *
	 * @return
	 */
	public boolean canEverTick() {
		return canEverTick;
	}

	@Override
	public String toString() {
		return "Entity{" + "ID=" + ID + ", components=" + components + '}';
	}

	public int getID() {
		return ID;
	}

	public void destroy() {
		this.ID = 0;
	}

}
