package cz.plesioEngine.guis;

import cz.plesioEngine.gameLogic.LogicMaster;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.textures.Texture;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class AnimatedGUITexture extends GuiTexture {

	private AnimStatus currentStatus = AnimStatus.NONE;

	public AnimatedGUITexture(Texture texture, Vector2f position,
		Vector2f scale) {
		super(texture, position, scale);
	}

	public void fadeInTick() {
		if (getTransparency() - 0.0075f > 0) {
			setTransparency(getTransparency() - 2 * DisplayManager.getDelta());
		} else {
			try {
				reset(AnimatedGUITexture.class.getMethod(
					"fadeInTick", (Class<?>[]) null), this);
			} catch (NoSuchMethodException | SecurityException ex) {
				Logger.getLogger(
					AnimatedGUITexture.class.getName()).
					log(Level.SEVERE, null, ex);
			}
		}
	}

	public void fadeOutTick() {
		if (getTransparency() + 0.0075f < 1) {
			setTransparency(getTransparency() + 2 * DisplayManager.getDelta());
		} else {
			try {
				reset(AnimatedGUITexture.class.getMethod(
					"fadeOutTick", (Class<?>[]) null), this);
			} catch (NoSuchMethodException | SecurityException ex) {
				Logger.getLogger(
					AnimatedGUITexture.class.getName()).
					log(Level.SEVERE, null, ex);
			}
		}
	}

	public void fadeIn() {
		if (!(currentStatus == AnimStatus.NONE)) {
			return;
		}
		currentStatus = AnimStatus.FADING;
		try {
			LogicMaster.registerMethodToRunEveryFrame(
				AnimatedGUITexture.class.getMethod(
					"fadeInTick", (Class<?>[]) null), this);
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(AnimatedGUITexture.class.
				getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void fadeOut() {
		if (!(currentStatus == AnimStatus.NONE)) {
			return;
		}
		currentStatus = AnimStatus.FADING;
		try {
			LogicMaster.registerMethodToRunEveryFrame(
				AnimatedGUITexture.class.getMethod(
					"fadeOutTick", (Class<?>[]) null), this);
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(AnimatedGUITexture.class.
				getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void reset(Method m, Object o) {
		LogicMaster.removePerFrameMethod(m, o);
		currentStatus = AnimStatus.NONE;
	}

	public AnimStatus getAnimStatus(){
		return currentStatus;
	}
	
}
