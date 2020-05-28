package cz.plesioEngine.ECS;

import java.util.ArrayList;
import java.util.List;
import cz.plesioEngine.ECS.systems.IComponentSystem;
import cz.plesioEngine.ECS.components.IComponent;
import java.util.stream.Collectors;

/**
 *
 * @author plesio
 */
public class EntityManager {

	private static List<IComponentSystem> systems = new ArrayList<>();
	private static List<Entity> entities = new ArrayList<>();
	private static List<List<IComponent>> components = new ArrayList<>();

	private static int entityIDCounter = 1;

	/**
	 * Runs per game cycle, updates all component systems
	 */
	public static void update() {
		//TOOD
	}

	/**
	 * Initializes component systems
	 */
	public static void createSystems() {
		for (IComponentSystem s : systems) {
			if (!s.init()) {
				System.out.println("Failed to create system.");
			}
		}
	}

	/**
	 * Don't use this method outside.
	 *
	 * @param component
	 * @return
	 */
	public static int createComponent(IComponent component) {

		List<IComponent> componentList = null;
		for (List<IComponent> cList : components) {
			if (cList.get(0).getClass().equals(component.getClass())) {
				componentList = cList;
			}
		}
		if (componentList != null) {
			componentList.add(component);
			sortSpecificList(component.getClass());
			return componentList.size() - 1;
		} else {
			componentList = new ArrayList<>();
			componentList.add(component);
			components.add(componentList);
			return 0;
		}

	}

	/**
	 * Returns a component list of given type
	 *
	 * @param <T> generic list type
	 * @param cType type of component requested
	 * @return List of IComponent, or null if not found
	 */
	public static <T extends IComponent> List<T> getComponentList(Class cType) {
		for (List<IComponent> cList : components) {
			if (cList.get(0).getClass().equals(cType)) {
				return (List<T>) cList;
			}
		}
		return null;
	}

	/**
	 * Get the list of all entities.
	 *
	 * @return
	 */
	public static List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Creates a new empty entity
	 *
	 * @param canEverTick if canEverTick is false, then the entity will
	 * never update in the component systems
	 * @return new entity
	 */
	public static Entity createEntity(boolean canEverTick) {
		Entity newEntity = new Entity(entityIDCounter++, canEverTick);
		entities.add(newEntity);
		return newEntity;
	}

	private static void sortLists() {
		//Sort based on entityID, ignore current list indexes and the assign new
		//Efficiency will definitely be key in the future, when the entity 
		//lists become more dynamic at runtime. But now it don't matter yo.
		for (List<IComponent> componentList : components) {
			componentList.sort((o1, o2) -> {
				return o1.getEntityID() - o2.getEntityID();
			});
			for (int i = 0; i < componentList.size(); i++) {
				componentList.get(i).setComponentListIndex(i);
			}
		}
	}

	/**
	 * List of components filtered by entityID of a given type
	 * @param <T> generic type
	 * @param cType desired type
	 * @param entityID ID of the entity
	 * @return list of components of cType, filtered by entity ID
	 */
	public static <T extends IComponent> List<T> getComponentListByEntity(
		Class cType, int entityID) {

		for (List<IComponent> list : components) {
			if (list.get(0).getClass().equals(cType)) {
				List<IComponent> returnList = list.stream()
					.filter(comp -> comp.getEntityID() == entityID)
					.collect(Collectors.toList());
				return (List<T>) returnList;
			}
		}

		return null;

	}

	/**
	 * Remove a specific component from its associated list.
	 *
	 * @param component
	 */
	public static void removeComponent(IComponent component) {
		for (List<IComponent> componentList : components) {
			if (componentList.get(0).getClass().equals(component.getClass())) {
				componentList.remove(component);
			}
		}
	}

	/**
	 * Remove all components by entity ID and given type
	 *
	 * @param cType
	 * @param ID entity ID.
	 */
	public static void removeAllComponentsByID(Class cType, int ID) {
		for (List<IComponent> componentList : components) {
			if (componentList.get(0).getClass().equals(cType)) {
				componentList.removeIf(c -> c.getEntityID() == ID);
			}
		}
	}

	private static void sortSpecificList(Class listType) {
		for (List<IComponent> componentList : components) {
			if (componentList.get(0).getClass().equals(listType)) {
				componentList.sort((o1, o2) -> {
					return o1.getEntityID() - o2.getEntityID();
				});
				for (int i = 0; i < componentList.size(); i++) {
					componentList.get(i).setComponentListIndex(i);
				}
				break;
			}
		}
	}

}
