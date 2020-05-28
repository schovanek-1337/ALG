package cz.plesioEngine.controls;

/**
 * Essentially a basket for keys.
 *
 * @author plesio
 */
public class ControlAction {

	/**
	 * ID of a key
	 */
	private final int keyID;

	/**
	 * Key's scancode, platform dependant.
	 */
	private final int scancode;

	/**
	 * The actions can be GLFW_RELEASE, GLFW_PRESS
	 */
	private final int action;

	/**
	 * Required modifier keys.
	 */
	private final int mods;

	/**
	 * Defines a control action which doesn't include a scancode.
	 *
	 * @param keyID ID of the desired key. Usual format: GLFW_KEY_XXX
	 * @param action Action of the key, usually GLFW_PRESS, GLFW_RELEASE
	 * @param mods Modifiers, GLFW_SHIFT, GLFW_LSHIFT, etc
	 */
	public ControlAction(int keyID, int action, int mods) {
		this.keyID = keyID;
		this.scancode = 0;
		this.action = action;
		this.mods = mods;
	}

	/**
	 * Defines a control action which doesn't include a scancode.
	 *
	 * @param keyID ID of the desired key. Usual format: GLFW_KEY_XXX
	 * @param scancode currently ignored.
	 * @param action Action of the key, usually GLFW_PRESS, GLFW_RELEASE
	 * @param mods Modifiers, GLFW_SHIFT, GLFW_LSHIFT, etc
	 */
	public ControlAction(int keyID, int scancode, int action, int mods) {
		this.keyID = keyID;
		this.scancode = scancode;
		this.action = action;
		this.mods = mods;
	}

	public int getKeyID() {
		return keyID;
	}

	/**
	 * Scancode, currently unused.
	 *
	 * @return
	 */
	public int getScancode() {
		return scancode;
	}

	/**
	 * Action when key activates.
	 *
	 * @return
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Key modifiers.
	 *
	 * @return
	 */
	public int getMods() {
		return mods;
	}

}
