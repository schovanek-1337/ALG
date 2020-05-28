package cz.plesioEngine.controls;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.renderEngine.DisplayManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Def ctrl actions.
 *
 * @author plesio
 */
public class DefaultControls {

	public static void init() {
		try {
			ControlManager.registerFunction(
				new ControlAction(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_RELEASE, 0),
				null, DefaultControls.class.getDeclaredMethod("escapeMenu", (Class<?>[]) null));
			ControlManager.registerFunction(
				new ControlAction(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, 0), null,
				DefaultControls.class.getDeclaredMethod("jump", (Class<?>[]) null));
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(DefaultControls.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected static void escapeMenu() {
		glfwSetWindowShouldClose(DisplayManager.getWindowHandle(), true);
	}

	public static void jump() {
		Camera.jump();
	}

}
