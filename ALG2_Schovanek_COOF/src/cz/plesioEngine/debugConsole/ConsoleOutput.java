package cz.plesioEngine.debugConsole;

import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class ConsoleOutput {

	public enum LogType {
		ERR, INFO, METHOD_OUTPUT
	}

	private static final float FONT_SIZE = 1f;
	private static final float MAX_LINE_LENGTH = 1f;
	private static final boolean CENTERED = false;
	private static final Vector2f POSITION = new Vector2f(0.01f, 0.5f);

	private static FontType font;

	private static final List<String> LOG_STRINGS = new ArrayList<>();
	private static final List<LogType> LOG_TYPES = new ArrayList<>();

	private static boolean printDebugInfo = true;

	public static void init() {
		Texture fontAtlas = TextureMaster.requestTexture("font/candara");
		font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/candara.fnt"));
	}

	private static void createText() {

		if (printDebugInfo) {
			GUIText debugInfoFPS
				= new GUIText("FPS: " + DisplayManager.getFPS(), FONT_SIZE,
					font, new Vector2f(0.05f, 0.95f), MAX_LINE_LENGTH, CENTERED);

			debugInfoFPS.setColor(0.5f, 1f, 0.3f);

			TextMaster.render();
			TextMaster.removeText(debugInfoFPS);
		}

		for (int i = 0; i < LOG_STRINGS.size(); i++) {
			POSITION.y += 0.025f;
			//POSITION.y += 0.05f;
			GUIText text = new GUIText(LOG_STRINGS.get(i), FONT_SIZE, font, POSITION,
				MAX_LINE_LENGTH, CENTERED);

			switch (LOG_TYPES.get(i)) {
				case ERR:
					text.setColor(1, 0.1f, 0.1f);
					break;
				case INFO:
					text.setColor(1, 1, 1);
					break;
				case METHOD_OUTPUT:
					text.setColor(0.5f, 1, 0.5f);
					break;
			}

			TextMaster.render();
			TextMaster.removeText(text);
		}
	}

	public static void clearLog() {
		LOG_STRINGS.clear();
		LOG_TYPES.clear();
	}

	private static void resetTextPosition() {
		POSITION.y = 0.5f;
	}

	public static void appendToLog(String s, LogType type) {
		if (LOG_STRINGS.size() > 15) {
			LOG_STRINGS.clear();
			LOG_TYPES.clear();
		}
		LOG_STRINGS.add(s);
		LOG_TYPES.add(type);
	}

	public static void update() {
		createText();
		resetTextPosition();
	}

}
