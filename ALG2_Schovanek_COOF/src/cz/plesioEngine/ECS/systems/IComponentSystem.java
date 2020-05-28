package cz.plesioEngine.ECS.systems;

/**
 *
 * @author plesio
 */
public interface IComponentSystem {

	/**
	 * Method run on system creation.
	 *
	 * @return
	 */
	boolean init();

	/**
	 * Method run per-frame per-system.
	 */
	void update();

}
