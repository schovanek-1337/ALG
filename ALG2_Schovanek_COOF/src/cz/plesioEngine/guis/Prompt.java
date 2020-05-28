package cz.plesioEngine.guis;

import cz.plesioEngine.controls.ControlAction;
import cz.plesioEngine.controls.ControlManager;
import cz.plesioEngine.controls.KeyboardManager;
import cz.plesioEngine.controls.KeyboardSubscriber;
import cz.plesioEngine.debugConsole.ConsoleInput;
import cz.plesioEngine.engineTester.MainMenu;
import cz.plesioEngine.gameLogic.LogicMaster;
import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author plesio
 */
public class Prompt implements KeyboardSubscriber {

	private final float FONT_SIZE = 2f;
	private final float MAX_LINE_LENGTH = 1f;
	private boolean centered = false;
	private Vector2f position = new Vector2f();

	private FontType font;
	private GUIText guiText;
	private String text = "";

	private boolean finished;
	private boolean enabled;

	public Prompt(boolean enabled) {
		Texture fontAtlas = TextureMaster.requestTexture("font/candara");
		font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/candara.fnt"));
		this.enabled = enabled;
		register();
		setupKeys();
		KeyboardManager.subscribe(this);
	}

	private void setupKeys() {

		ControlAction backspaceText
			= new ControlAction(GLFW.GLFW_KEY_BACKSPACE, GLFW.GLFW_RELEASE, 0);
		ControlAction returnKey
			= new ControlAction(GLFW.GLFW_KEY_ENTER, GLFW.GLFW_RELEASE, 0);

		try {
			ControlManager.registerFunction(backspaceText, this,
				Prompt.class.getMethod("backspaceText", (Class<?>[]) null));
			ControlManager.registerFunction(returnKey, this,
				Prompt.class.getMethod("confirm", (Class<?>[]) null));
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(ConsoleInput.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
	
	public void resetPrompt(){
		text = "";
		finished = false;
		enabled = false;
		KeyboardManager.subscribe(this);
		register();
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void confirm() {
		if (!enabled) {
			return;
		}
		MainMenu.notifyReaderDone(text);
		KeyboardManager.unsubscribe(this);
		unregister();
		enabled = false;
	}

	public void backspaceText() {
		if (!enabled) {
			return;
		}
		if (text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
	}

	public void setEnabled(boolean val) {
		this.enabled = val;
	}

	private void register() {
		try {
			LogicMaster.registerMethodToRunEveryFrame(
				Prompt.class.getMethod("update",
					(Class<?>[]) null), this);
		} catch (NoSuchMethodException
			| SecurityException ex) {
			Logger.getLogger(
				Prompt.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}

	private void unregister() {
		try {
			LogicMaster.removePerFrameMethod(
				Prompt.class.getMethod("update",
					(Class<?>[]) null), this);
		} catch (NoSuchMethodException
			| SecurityException ex) {
			Logger.getLogger(
				Prompt.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}

	private void createText() {
		guiText = new GUIText(text, FONT_SIZE, font, position,
			MAX_LINE_LENGTH, centered);
		guiText.setColor(1, 1, 1);
	}

	private void registerKeyboardEvent(int codepoint) {
		char newCharacter = (char) codepoint;
		changeText(newCharacter);
	}

	private void changeText(char newCharacter) {
		text = text + String.valueOf(newCharacter);
	}

	public void update() {
		if (!enabled) {
			return;
		}
		createText();
		TextMaster.render();
		guiText.remove();
		if (finished) {
			unregister();
		}
	}

	@Override
	public void notifyKey(int codepoint) {
		if (enabled) {
			registerKeyboardEvent(codepoint);
		}
	}

}
