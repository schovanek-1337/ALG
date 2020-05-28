package cz.plesioEngine.controls;

import cz.plesioEngine.renderEngine.DisplayManager;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import org.lwjgl.glfw.GLFWCharCallback;

/**
 *
 * @author plesio
 */
public class KeyboardManager {

	private static List<KeyboardSubscriber> subscribers = new ArrayList<>();

	public static void init() {
		glfwSetCharCallback(DisplayManager.getWindowHandle(), new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				for (KeyboardSubscriber s : subscribers) {
					s.notifyKey(codepoint);
				}
			}
		});
	}

	public static void subscribe(KeyboardSubscriber ksb) {
		if (!subscribers.contains(ksb)) {
			subscribers.add(ksb);
		}
	}

	public static void unsubscribe(KeyboardSubscriber ksb) {
		if (subscribers.contains(ksb)) {
			subscribers.remove(ksb);
		}
	}

}
