package cz.plesioEngine.debugConsole;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.gameLogic.SettingMaster;
import cz.plesioEngine.toolbox.MousePicker;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector3f;

/**
 * Console class containing default commands.
 *
 * @author plesio
 */
public class Console {

	public static void init() {
		ConsoleInput.init();
		ConsoleOutput.init();

		try {
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("printCameraLocation",
					(Class<?>[]) null), "o_cpos");
			ConsoleInput.registerMethod(null,
				ConsoleInput.class.getMethod("listCommands",
					(Class<?>[]) null), "help");
			ConsoleInput.registerMethod(null,
				EntityRenderer.class.getMethod("setRenderBoundingBoxes",
					(Class<?>[]) null), "r_bboxes");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("printCameraDirection",
					(Class<?>[]) null), "o_cd");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("addScore",
					Integer.TYPE), "addscore");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("setScore",
					Integer.TYPE), "setscore");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("tp",
					Vector3f.class), "tp");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("toggleAI",
					(Class<?>[]) null), "tai");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("toggleMusic",
					(Class<?>[]) null), "tmusic");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("toggleGore",
					(Class<?>[]) null), "tgore");
			ConsoleInput.registerMethod(null,
				Console.class.getMethod("clear",
					(Class<?>[]) null), "clr");
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void tp(Vector3f location){
		Camera.setPosition(location);
	}
	
	public static void clear(){
		ConsoleOutput.clearLog();
	}
	
	public static void toggleAI(){
		SettingMaster.enableAI = !SettingMaster.enableAI;
	}
	
	public static void toggleMusic(){
		SettingMaster.enableMusic = !SettingMaster.enableMusic;
	}
	
	public static void toggleGore(){
		SettingMaster.enableGore = !SettingMaster.enableGore;
	}
	
	public static void setGameLevelFinish(){
		GameStateMaster.setGameState(GameState.FINISHED_LEVEL);
	}
	
	public static void update() {
		ConsoleInput.update();
		ConsoleOutput.update();
	}

	public static void addScore(int amount) {
		PlayerStats.score += amount;
	}

	public static void setScore(int value) {
		PlayerStats.score = value;
	}

	public static void printCameraLocation() {
		String output = "X: " + Camera.getPositionInstance().x()
			+ " Y: " + Camera.getPositionInstance().y()
			+ " Z: " + Camera.getPositionInstance().z();
		ConsoleOutput.appendToLog(output,
			ConsoleOutput.LogType.METHOD_OUTPUT);
	}

	public static void printCameraDirection() {
		String output = "X: " + MousePicker.getCurrentRay().x()
			+ " Y: " + MousePicker.getCurrentRay().y()
			+ " Z: " + MousePicker.getCurrentRay().z();
		ConsoleOutput.appendToLog(output,
			ConsoleOutput.LogType.METHOD_OUTPUT);
	}

}
