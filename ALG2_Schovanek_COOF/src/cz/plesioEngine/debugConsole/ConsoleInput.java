package cz.plesioEngine.debugConsole;

import cz.plesioEngine.controls.ControlAction;
import cz.plesioEngine.controls.ControlManager;
import cz.plesioEngine.controls.KeyboardManager;
import cz.plesioEngine.controls.KeyboardSubscriber;
import cz.plesioEngine.controls.MouseManager;
import java.io.File;

import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class ConsoleInput {

	private static final float FONT_SIZE = 1f;
	private static final float MAX_LINE_LENGTH = 1f;
	private static final boolean CENTERED = false;
	private static final Vector2f POSITION = new Vector2f(0.01f, 0.97f);

	private static String text = "";
	private static GUIText guiText;
	private static FontType font;

	private static boolean isTyping = false;

	private static final Map<String, Method> METHOD_MAP = new HashMap<>();
	private static final Map<String, Object> OBJECT_MAP = new HashMap<>();

	private static KeyboardSubscriber keyboard = (int codepoint) -> {
		registerKeyboardEvent(codepoint);
	};
	
	public static void init() {
		Texture fontAtlas = TextureMaster.requestTexture("font/candara");
		font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/candara.fnt"));
		setupKeys();
		KeyboardManager.subscribe(keyboard);
	}

	private static void setupKeys() {

		ControlAction enableConsole
			= new ControlAction(GLFW.GLFW_KEY_GRAVE_ACCENT, GLFW.GLFW_RELEASE, 0);
		ControlAction backspaceText
			= new ControlAction(GLFW.GLFW_KEY_BACKSPACE, GLFW.GLFW_RELEASE, 0);
		ControlAction returnKey
			= new ControlAction(GLFW.GLFW_KEY_ENTER, GLFW.GLFW_RELEASE, 0);

		try {
			ControlManager.registerFunction(enableConsole, null,
				ConsoleInput.class.getMethod("enableConsole", (Class<?>[]) null));
			ControlManager.registerFunction(backspaceText, null,
				ConsoleInput.class.getMethod("backspaceText", (Class<?>[]) null));
			ControlManager.registerFunction(returnKey, null,
				ConsoleInput.class.getMethod("changeEngineState", (Class<?>[]) null));
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(ConsoleInput.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Ignore, is public because of control master's inner workings
	 */
	public static void backspaceText() {
		if (text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
	}

	/**
	 * Ignore, is public because of control master's inner workings
	 */
	public static void enableConsole() {
		isTyping = !isTyping;
		text = "";
		if (isTyping) {
			GameStateMaster.setGameState(GameState.PAUSED);
			MouseManager.requestMouseState(GLFW.GLFW_CURSOR_NORMAL);
		} else {
			if (GameStateMaster.getCurrentGameState() != null) {
				GameStateMaster.setGameState(GameStateMaster.getPreviousGameState());
			}
			if (GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
				MouseManager.releaseMouseState(true);
			} else {
				MouseManager.releaseMouseState(false);
			}
		}
	}

	/**
	 * Ignore, is public because of control master's inner workings
	 */
	public static void changeEngineState() {
		if (!text.isEmpty()) {
			ConsoleOutput.appendToLog(text, ConsoleOutput.LogType.INFO);

			findMethodAndExecute();

			text = "";
			isTyping = false;
			if (GameStateMaster.getCurrentGameState() != null) {
				GameStateMaster.setGameState(GameStateMaster.getPreviousGameState());
			}
			if (GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
				MouseManager.releaseMouseState(true);
			} else {
				MouseManager.releaseMouseState(false);
			}
		}
	}

	private static void createText() {
		guiText = new GUIText(text, FONT_SIZE, font, POSITION,
			MAX_LINE_LENGTH, CENTERED);
		guiText.setColor(1, 1, 1);
	}

	private static void destroyText() {
		guiText.remove();
	}

	private static void changeText(char newCharacter) {
		text = text + String.valueOf(newCharacter);
	}

	private static void registerKeyboardEvent(int codepoint) {
		if (isTyping) {
			char newCharacter = (char) codepoint;
			changeText(newCharacter);
		}
	}

	public static boolean getIsTyping() {
		return isTyping;
	}

	public static void registerMethod(Object obj, Method method, String command) {
		METHOD_MAP.put(command, method);
		OBJECT_MAP.put(command, obj);
	}

	private static void changeTextState() {
		createText();
	}

	public static void update() {
		changeTextState();
		TextMaster.render();
		destroyText();
	}

	public static void listCommands() {
		for (Map.Entry pair : METHOD_MAP.entrySet()) {
			ConsoleOutput.appendToLog(pair.getKey() + " " + pair.getValue(),
				ConsoleOutput.LogType.METHOD_OUTPUT);
		}
	}

	public static Object[] parseTypesAndConvert(Class<?>[] types,
		String[] arguments) {
		Object[] out = new Object[types.length];

		for (int i = 0; i < types.length; i++) {

			if (types[i].equals(Float.TYPE)) {
				out[i] = Float.parseFloat(arguments[i]);
				continue;
			}
			
			if (types[i].equals(Integer.TYPE)) {
				out[i] = Integer.parseInt(arguments[i]);
				continue;
			}

			if (types[i].equals(Vector3f.class)) {
				Vector3f newVector = new Vector3f();

				newVector.x = Float.parseFloat(arguments[i]);
				newVector.y = Float.parseFloat(arguments[i + 1]);
				newVector.z = Float.parseFloat(arguments[i + 2]);

				out[i] = newVector;

				continue;
			}

		}

		return out;
	}

	private static void findMethodAndExecute() {
		String firstCommand;
		String[] arguments;

		firstCommand = text.split(" ")[0];

		text = text.replace(firstCommand + " ", "");

		arguments = text.split(" ");

		if (!METHOD_MAP.containsKey(firstCommand)) {
			ConsoleOutput.appendToLog("Unknown command: " + firstCommand,
				ConsoleOutput.LogType.ERR);
			return;
		}
		try {
			Method getMethod = METHOD_MAP.get(firstCommand);
			Object obj = OBJECT_MAP.get(firstCommand);
			if (getMethod.getParameterCount() == 0) {
				getMethod.invoke(obj, (Object[]) null);
			} else {
				Class<?>[] types = getMethod.getParameterTypes();
				Object[] argumentObjects = parseTypesAndConvert(types, arguments);

				getMethod.invoke(obj, argumentObjects);
			}

		} catch (ArrayIndexOutOfBoundsException | IllegalAccessException
			| IllegalArgumentException | InvocationTargetException ex) {
			ConsoleOutput.appendToLog(ex.toString(), ConsoleOutput.LogType.ERR);
		}

	}

}
