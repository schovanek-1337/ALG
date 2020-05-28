package cz.plesioEngine.engineTester;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.audio.Source;
import cz.plesioEngine.controls.ControlManager;
import cz.plesioEngine.controls.MouseManager;
import cz.plesioEngine.debugConsole.Console;
import cz.plesioEngine.entities.enemies.AI.AIMaster;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.entities.weapons.WeaponMaster;
import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.gameLogic.LogicMaster;
import cz.plesioEngine.gameLogic.Overkill;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.gameLogic.levels.Room;
import cz.plesioEngine.gameLogic.music.MusicTrigger;
import cz.plesioEngine.gameLogic.score.ScoreLoader;
import cz.plesioEngine.guis.GuiRenderer;
import cz.plesioEngine.guis.GuiTexture;
import cz.plesioEngine.guis.Menu;
import cz.plesioEngine.guis.MenuItem;
import cz.plesioEngine.renderEngine.particles.SimpleParticleSystem;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.gameLogic.score.CompareByScore;
import cz.plesioEngine.gameLogic.score.CompareByTime;
import cz.plesioEngine.gameLogic.score.Scoreboard;
import cz.plesioEngine.guis.AnimStatus;
import cz.plesioEngine.guis.AnimatedGUITexture;
import cz.plesioEngine.guis.HUD;
import cz.plesioEngine.guis.Prompt;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.renderEngine.particles.Giblet;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import cz.plesioEngine.toolbox.MousePicker;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 *
 * @author plesio
 */
public class MainMenu {

	private static int mainMenuMode = 0;
	private static int sortType = 0;

	public static void main(String[] args) {
		long windowHandle;

		DisplayManager.createDisplay();
		windowHandle = DisplayManager.getWindowHandle();

		GameStateMaster.init(GameState.MAIN_MENU);

		AudioMaster.init();
		int mainMenu = AudioMaster.loadSound("res/soundtrack/mainMenu.ogg");
		int missionComplete = AudioMaster.loadSound("res/soundtrack/levelComplete.ogg");
		int gameplay = AudioMaster.loadSound("res/soundtrack/gameplay-bfgd.ogg");

		Camera.init();

		ControlManager.init();
		MasterRenderer.init();
		Console.init();

		Overkill.init();

		MousePicker.init();

		Texture fontAtlas = TextureMaster.requestTexture("font/candara");
		FontType font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/candara.fnt"));

		GuiTexture logo = new GuiTexture(TextureMaster.requestTexture("logo"),
			new Vector2f(0, 0.4f), new Vector2f(0.5f, 0.35f));
		GuiRenderer.processGUI(logo);

		GUIText playText = new GUIText("PLAY", 2, font, new Vector2f(0, 0.5f), 1, true);
		playText.setColor(1, 1, 1);

		GUIText scoreboardText = new GUIText("SCOREBOARD", 2, font, new Vector2f(0, 0.6f), 1, true);
		scoreboardText.setColor(1, 1, 1);

		GUIText quitText = new GUIText("QUIT", 2, font, new Vector2f(0, 0.7f), 1, true);
		quitText.setColor(1, 1, 1);

		GUIText sortTypeText = new GUIText("CHANGE SORT", 2, font, new Vector2f(0, 0.8f), 1, true);
		sortTypeText.setColor(1, 1, 1);
		sortTypeText.remove();

		GUIText deleteScore = new GUIText("CLEAR SCOREBOARD", 2, font, new Vector2f(0, 0.85f), 1, true);
		deleteScore.setColor(1, 1, 1);
		deleteScore.remove();

		GUIText backText = new GUIText("BACK", 2, font, new Vector2f(0, 0.9f), 1, true);
		backText.setColor(1, 1, 1);
		backText.remove();

		Texture fireAtlas = TextureMaster.requestTexture("fire", 8);
		SimpleParticleSystem sps = new SimpleParticleSystem(10, 10, 20, 2, fireAtlas);

		Source musicSource = new Source();
		musicSource.setVolume(1f);
		musicSource.setLooping(true);
		musicSource.setPosition(new Vector3f(0, 0, 0));
		musicSource.play(mainMenu);

		Source gameplayMusicSource = new Source();
		gameplayMusicSource.setVolume(1f);
		gameplayMusicSource.setLooping(true);
		gameplayMusicSource.setPosition(new Vector3f(0, 0, 0));

		Menu mainMenuSystem = new Menu();

		Giblet g = new Giblet(new Vector3f(0, 0, 0), 0, 0, 0, 1);

		Menu backToMainMenu = new Menu();

		GUIText backTextScore = new GUIText("MAIN MENU", 2, font, new Vector2f(0, 0.85f), 1, true);
		backTextScore.setColor(1, 1, 1);
		backTextScore.remove();

		try {
			MenuItem playMenuItem = new MenuItem(playText, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("mainMenuPlay", (Class<?>[]) null)));
			MenuItem scoreBoardMenuItem = new MenuItem(scoreboardText, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("scoreboard", (Class<?>[]) null)));
			MenuItem quitMenuItem = new MenuItem(quitText, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("mainMenuQuit", (Class<?>[]) null)));
			MenuItem sortItem = new MenuItem(sortTypeText, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("changeSort", (Class<?>[]) null)));
			MenuItem deleteScoreItem = new MenuItem(deleteScore, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("deleteScore", (Class<?>[]) null)));
			MenuItem backMenuItem = new MenuItem(backText, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("back", (Class<?>[]) null)));
			MenuItem backtoMainMenuItem = new MenuItem(backTextScore, Pair.with(
				MainMenu.class, MainMenu.class.getMethod("backToMainMenu", (Class<?>[]) null)));
			mainMenuSystem.addOption(playMenuItem);
			mainMenuSystem.addOption(scoreBoardMenuItem);
			mainMenuSystem.addOption(quitMenuItem);
			mainMenuSystem.addOption(sortItem);
			mainMenuSystem.addOption(deleteScoreItem);
			mainMenuSystem.addOption(backMenuItem);
			backToMainMenu.addOption(backtoMainMenuItem);
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
		}

		MouseManager.setActiveMenu(mainMenuSystem);

		HUD mainHud = new HUD();

		int humSound = AudioMaster.loadSound("res/sound/ambience/hum.ogg");

		Source ambienceSource = new Source();
		ambienceSource.setLooping(true);
		ambienceSource.setVolume(20);

		AnimatedGUITexture youDied = new AnimatedGUITexture(TextureMaster.requestTexture("gui/youdied"),
			new Vector2f(), new Vector2f(1.8f, 1f));
		youDied.setTransparency(1);

		AnimatedGUITexture levelComplete = new AnimatedGUITexture(TextureMaster.requestTexture("gui/complete"),
			new Vector2f(), new Vector2f(1.8f, 1f));

		levelComplete.setTransparency(1);

		AnimatedGUITexture blackScreen
			= new AnimatedGUITexture(TextureMaster.requestTexture("gui/blackscreen"),
				new Vector2f(), new Vector2f(2, 1));

		Prompt namePrompt = new Prompt(false);
		namePrompt.setCentered(true);
		namePrompt.setPosition(new Vector2f(0, 0.5f));

		Scoreboard.init();

		boolean finishScreenDone = false;
		boolean postGameCleanupDone = true;
		
		MusicTrigger mt = new MusicTrigger(new Vector3f(-25.3f, 13, -267), gameplay);

		while (!GLFW.glfwWindowShouldClose(windowHandle)) {

			if (MousePicker.getCurrentRay() != null) {
				AudioMaster.setListenerData(Camera.getPositionReference(),
					Camera.getDirection());
			}

			switch (GameStateMaster.getCurrentGameState()) {
				case PLAYING:
					GuiRenderer.removeGUI(logo);
					playText.remove();
					scoreboardText.remove();
					quitText.remove();
					if (!ambienceSource.isPlaying()) {
						ambienceSource.play(humSound);
					}
					if (musicSource.isPlaying()) {
						musicSource.stop();
					}
					//if (!gameplayMusicSource.isPlaying()
					//	&& SettingMaster.enableMusic) {
					//	gameplayMusicSource.play(gameplay);
					//}
					break;
				case MENU:
					break;
				case MAIN_MENU:
					if (GameStateMaster.getPreviousGameState()
						== GameState.FINISHED_LEVEL) {
						if (!postGameCleanupDone) {
							GuiRenderer.clear();
							TextMaster.clear();
							MouseManager.setActiveMenu(mainMenuSystem);
							finishScreenDone = false;
							LogicMaster.clearMethods();
							Scoreboard.reset();
							PhysicsEngine.clear();
							namePrompt.resetPrompt();
							namePrompt.setEnabled(false);
							postGameCleanupDone = true;
							PlayerStats.health.resetHealth();
							PlayerStats.score = 0;
							AIMaster.clear();
							mt.reset();
						}
					}
					switch (mainMenuMode) {
						case 0:
							TextMaster.loadText(playText);
							TextMaster.loadText(scoreboardText);
							TextMaster.loadText(quitText);
							TextMaster.removeText(backText);
							TextMaster.removeText(sortTypeText);
							TextMaster.removeText(deleteScore);
							GuiRenderer.processGUI(logo);
							break;
						case 1:
							TextMaster.removeText(playText);
							TextMaster.removeText(scoreboardText);
							TextMaster.removeText(quitText);
							TextMaster.loadText(backText);
							TextMaster.loadText(deleteScore);
							TextMaster.loadText(sortTypeText);
							GuiRenderer.removeGUI(logo);
							break;
					}
					sps.generateParticles(new Vector3f(0, 5, -10));
					break;
				case GAME_OVER:
					youDied.fadeIn();
					GuiRenderer.clear();
					GuiRenderer.processGUI(youDied);
					break;
				case FINISHED_LEVEL:

					if (!musicSource.isPlaying()) {
						musicSource.play(missionComplete);
					}
					if (mt.getSource().isPlaying()) {
						mt.getSource().stop();
					}

					if (!finishScreenDone) {
						blackScreen.fadeOut();
						levelComplete.fadeIn();
						GuiRenderer.clear();
						GuiRenderer.processGUI(levelComplete);
						GuiRenderer.processGUI(blackScreen);
						EntityRenderer.clear();
						Room.removeRooms();
						finishScreenDone = true;
						namePrompt.setEnabled(true);
					}

					if (blackScreen.getAnimStatus() == AnimStatus.NONE) {
						GUIText scoreText = new GUIText("SCORE: "
							+ PlayerStats.score, 2, font,
							new Vector2f(0.1f, 0.8f), 1, false);
						scoreText.setColor(1, 1, 1);

						GUIText timeText = new GUIText("TIME: "
							+ HUD.df.format(PlayerStats.time), 2, font,
							new Vector2f(0.1f, 0.85f), 1, false);
						timeText.setColor(1, 1, 1);

						GUIText namePromptText = new GUIText(
							"ENTER NAME FOR SCOREBOARD", 2, font,
							new Vector2f(0, 0.45f), 1, true);
						namePromptText.setColor(1, 1, 1);

						TextMaster.loadText(backTextScore);

						MouseManager.setActiveMenu(backToMainMenu);

						Camera.setPosition(new Vector3f(0, 0, 0));
						Camera.setRoll(0);
						Camera.setYaw(0);
						Camera.setPitch(0);

						sps.generateParticles(new Vector3f(0, 5, -10));

						postGameCleanupDone = false;

					}

					break;
			}

			DisplayManager.updateFPS();

			Camera.move();

			MouseManager.updateFrameSensitiveEvents();

			PhysicsEngine.update();

			AIMaster.update();

			MasterRenderer.renderScene();

			LogicMaster.everyFrameUpdate();

			if (GameStateMaster.getCurrentGameState()
				== GameState.MAIN_MENU && mainMenuMode == 1) {
				if (sortType == 0) {
					Scoreboard.printScoreboard(new CompareByScore(), new Vector2f(0.15f, 0.1f));
				} else {
					Scoreboard.printScoreboard(new CompareByTime(), new Vector2f(0.15f, 0.1f));
				}

			}

			if (GameStateMaster.getCurrentGameState()
				== GameState.PLAYING) {
				mainHud.update();
				mainHud.addHuds(Triplet.with(WeaponMaster.getHeldWeapon().getCurrentAmmo(), " :AMMO", new Vector2f(0.75f, 0.9f)));
				mainHud.addHuds(Triplet.with("HEALTH: ", PlayerStats.health.getHealth(), new Vector2f(0.1f, 0.9f)));
				mainHud.addHuds(Triplet.with("SCORE: ", PlayerStats.score, new Vector2f(0.1f, 0.1f)));
				mainHud.addHuds(Triplet.with("TIME: ", PlayerStats.time, new Vector2f(0.1f, 0.15f)));
				PlayerStats.time += DisplayManager.getDelta();
			}

			Console.update();

			DisplayManager.updateUPS();

			DisplayManager.updateDisplay();

		}

		AudioMaster.cleanUp();

		DisplayManager.closeDisplay();

		MasterRenderer.cleanUp();
	}

	public static void scoreboard() {
		mainMenuMode = 1;
	}

	public static void back() {
		mainMenuMode = 0;
	}

	public static void changeSort() {
		if (sortType == 0) {
			sortType = 1;
		} else {
			sortType = 0;
		}
	}

	public static void backToMainMenu() {
		GameStateMaster.setGameState(GameState.MAIN_MENU);
	}

	public static void deleteScore() {
		Scoreboard.delete();
		File scoreFile = new File("./data/score.txt");
		if (scoreFile.exists()) {
			scoreFile.delete();

		}
	}

	public static void mainMenuPlay() {
		E1M1.load();
		MouseManager.resetActiveMenu();
		GameStateMaster.setGameState(GameState.PLAYING);
	}

	public static void mainMenuQuit() {
		glfwSetWindowShouldClose(DisplayManager.getWindowHandle(), true);
	}

	public static void notifyReaderDone(String result) {

		Texture fontAtlas = TextureMaster.requestTexture("font/candara");
		FontType font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/candara.fnt"));

		ScoreLoader.saveScore(result);

		GUIText savedText = new GUIText(
			"SAVED!", 2, font,
			new Vector2f(0, 0.55f), 1, true);
		savedText.setColor(0, 0.9f, 0);
	}

}
