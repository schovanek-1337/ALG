package cz.plesioEngine.controls;

import cz.plesioEngine.renderEngine.DisplayManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * Maps actions to keys
 *
 * @author plesio
 */
public class ControlManager {

	private static final Map<ControlAction, Method> METHOD_MAP = new HashMap<>();
	private static final Map<ControlAction, Object> OBJECT_MAP = new HashMap<>();

	/**
	 * Sets the callback, don't initialize if you want it to not work.
	 */
	public static void init() {
		DefaultControls.init();
		MouseManager.init();
		KeyboardManager.init();
		glfwSetKeyCallback(DisplayManager.getWindowHandle(), (window, key,
			scancode, action, mods) -> {
			parseRegisteredFunctions(key, scancode, action, mods);
		});
	}

	/**
	 * Register a function which will be parsed on key callback.
	 *
	 * @param key ControlAction, which defines the key mods, etc.
	 * @param object The object which is used for method invocation.
	 * @param method
	 * @return
	 */
	public static boolean registerFunction(ControlAction key, Object object,
		Method method) {

		//if (METHOD_MAP.containsKey(key) && OBJECT_MAP.containsKey(key)) {
		//	return false;
		//}
		
		METHOD_MAP.put(key, method);
		OBJECT_MAP.put(key, object);

		return true;
	}

	private static void parseRegisteredFunctions(int key, int scancode,
		int action, int mods) {

		for (Map.Entry pair : METHOD_MAP.entrySet()) {
			ControlAction temporary = (ControlAction) pair.getKey();
			if (temporary.getKeyID() == key
				&& temporary.getAction() == action
				&& temporary.getMods() == mods) {

				Method getMethod = METHOD_MAP.get(temporary);
				Object object = OBJECT_MAP.get(temporary);

				try {
					getMethod.invoke(object, (Object[]) null);
				} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException ex) {
					Logger.getLogger(ControlManager.class.getName())
						.log(Level.SEVERE, null, ex);
				}

			}
		}

	}

}
