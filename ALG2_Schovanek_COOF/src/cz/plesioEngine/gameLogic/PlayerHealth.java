package cz.plesioEngine.gameLogic;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.audio.Source;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.guis.AnimatedGUITexture;
import cz.plesioEngine.guis.GuiRenderer;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class PlayerHealth {

	private int value = 100;
	private int maxHealth = 100;

	private static float healthHurtCooldown = 0.3f;
	private static float healthHurtCooldownCurrent = 0;

	private int playerHurtSoundBuffer;
	private int playerDeathSoundBuffer;
	private Source playerSource = new Source();

	private static AnimatedGUITexture texture
		= new AnimatedGUITexture(
			TextureMaster.requestTexture("gui/hurt"),
			new Vector2f(0, 0),
			new Vector2f(1.8f, 1f));

	public PlayerHealth() {
		playerHurtSoundBuffer = AudioMaster.loadSound("res/sound/hurt.ogg");
		playerDeathSoundBuffer = AudioMaster.loadSound("res/sound/death.ogg");
		playerSource.setVolume(1);
		register();
	}

	private void register() {
		try {
			LogicMaster.registerMethodToRunEveryFrame(
				PlayerHealth.class.getMethod("update", (Class<?>[]) null),
				this);
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(PlayerHealth.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void resetHealth(){
		value = 100;
		maxHealth = 100;
	}

	/**
	 * Changes health if it isn't on cool-down.
	 *
	 * @param amount gee i wonder what this could mean.
	 */
	public void changeHealth(int amount) {
		if (amount < 0) {
			if (healthHurtCooldownCurrent <= 0) {
				if (value + amount <= 0) {
					GameStateMaster.setGameState(GameState.GAME_OVER);
					Camera.setPitch(-70);
					Camera.getPositionReference().y -= 6;
					playerSource.play(playerDeathSoundBuffer);
					return;
				}
				value += amount;
				hurt();
				playerSource.play(playerHurtSoundBuffer);
				healthHurtCooldownCurrent = healthHurtCooldown;
			}
		} else {
			value += amount;
			if(value > 100){
				value = 100;
			}
		}
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Updates the hurt cool-down.
	 */
	public void update() {
		if (healthHurtCooldownCurrent > 0) {
			healthHurtCooldownCurrent -= DisplayManager.getDelta();
		}
	}

	public int getHealth() {
		return value;
	}

	private void reset() {
		texture.setTransparency(0);
	}

	private void hurt() {
		reset();
		GuiRenderer.processGUI(texture);
		texture.fadeOut();
	}

}
