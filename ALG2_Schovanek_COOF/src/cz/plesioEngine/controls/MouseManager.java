package cz.plesioEngine.controls;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.weapons.WeaponMaster;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.guis.Menu;
import cz.plesioEngine.guis.MenuItem;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.toolbox.MousePicker;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

/**
 * Has mouse callbacks and states.
 *
 * @author plesio
 */
public final class MouseManager {

	private static int currentMouseState;

	private static String currentLockOwner;

	private static Menu currentMenu;
	private static MenuItem currentHoverOption;

	private MouseManager() {

	}

	public static void init() {

		currentMouseState = GLFW.GLFW_CURSOR_NORMAL;

		glfwSetCursorPosCallback(DisplayManager.getWindowHandle(), (window,
			xpos, ypos) -> {

			if (GameStateMaster.getCurrentGameState() == GameState.MAIN_MENU
				|| GameStateMaster.getCurrentGameState() == GameState.FINISHED_LEVEL) {

				if (currentMenu != null) {
					Vector2f coords
						= getNormalizedDeviceCoords((float) xpos, (float) ypos);

					currentHoverOption = currentMenu.getHover(coords.x(), coords.y());
				}

			}

			if (GameStateMaster.getCurrentGameState() == GameState.FINISHED_LEVEL) {
				currentMouseState = GLFW.GLFW_CURSOR_NORMAL;
				glfwSetInputMode(DisplayManager.getWindowHandle(), GLFW_CURSOR,
					currentMouseState);
			}

			MousePicker.update();

			if (currentMouseState == GLFW.GLFW_CURSOR_DISABLED
				&& GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
				Camera.setPitch(Camera.getPitch() - DisplayManager.getMouseDelta().y * 7.5f);
				Camera.setYaw(Camera.getYaw() - DisplayManager.getMouseDelta().x * 7.5f);
				GLFW.glfwSetCursorPos(DisplayManager.getWindowHandle(),
					DisplayManager.getWindowDimensions().y / 2f,
					DisplayManager.getWindowDimensions().x / 2f);
			}
		});
		glfwSetMouseButtonCallback(DisplayManager.getWindowHandle(), (long window,
			int button, int action, int mods) -> {
			if (action == GLFW.GLFW_PRESS) {
				if (currentHoverOption != null) {
					currentHoverOption.activate();
				}
				currentHoverOption = null;
			}
		});
		GLFW.glfwSetScrollCallback(DisplayManager.getWindowHandle(), ((window, xoffset, yoffset) -> {
			Camera.jump();
		}));

	}

	public static void setActiveMenu(Menu m) {
		currentMenu = m;
	}

	public static void resetActiveMenu() {
		currentMenu = null;
	}

	public static void updateFrameSensitiveEvents() {
		if (GLFW.glfwGetMouseButton(DisplayManager.getWindowHandle(),
			GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
			if (GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
				WeaponMaster.shoot();
			}
		}
		if (GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
			currentMouseState = GLFW.GLFW_CURSOR_DISABLED;
			glfwSetInputMode(DisplayManager.getWindowHandle(), GLFW_CURSOR,
				currentMouseState);
		}
	}

	public static boolean updateMouseState(int state) {
		StackTraceElement[] stackTraceElements
			= Thread.currentThread().getStackTrace();
		String caller = stackTraceElements[2].getClassName();
		if (currentLockOwner.equals(caller)) {
			currentMouseState = state;
			glfwSetInputMode(DisplayManager.getWindowHandle(), GLFW_CURSOR,
				currentMouseState);
			return true;
		}
		return false;
	}

	public static boolean requestMouseState(int state) {
		StackTraceElement[] stackTraceElements
			= Thread.currentThread().getStackTrace();
		String caller = stackTraceElements[2].getClassName();
		if (currentLockOwner == null) {
			currentMouseState = state;
			currentLockOwner = caller;
			glfwSetInputMode(DisplayManager.getWindowHandle(), GLFW_CURSOR,
				currentMouseState);
			return true;
		}
		return false;
	}

	public static void releaseMouseState(boolean hideMouse) {
		StackTraceElement[] stackTraceElements
			= Thread.currentThread().getStackTrace();
		String caller = stackTraceElements[2].getClassName();
		if (caller.equals(currentLockOwner)) {
			currentLockOwner = null;
			if (hideMouse) {
				currentMouseState = GLFW.GLFW_CURSOR_DISABLED;
				glfwSetInputMode(DisplayManager.getWindowHandle(), GLFW_CURSOR,
					GLFW.GLFW_CURSOR_DISABLED);
			}
		}
	}

	public static Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (mouseX) / DisplayManager.getWindowDimensions().y();
		float y = (mouseY) / DisplayManager.getWindowDimensions().x();

		return new Vector2f(x, y);
	}

	public static Object getLockOwner() {
		return currentLockOwner;
	}

	public static int getCurrentMouseState() {
		return currentMouseState;
	}

}
