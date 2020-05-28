package cz.plesioEngine.gameLogic.levels;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.gameLogic.LogicMaster;
import cz.plesioEngine.toolbox.FuzzyMaths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class LevelFinish {

	private Vector3f position;
	private float tolerance;

	public LevelFinish(Vector3f position, float tolerance) {
		this.position = position;
		this.tolerance = tolerance;
		register();
	}

	private boolean checkFinishLevel() {
		return FuzzyMaths.compareVectorsFuzzy(position,
			Camera.getPositionInstance(), tolerance);
	}

	public void update() {
		if (checkFinishLevel() && GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
			GameStateMaster.setGameState(GameState.FINISHED_LEVEL);
		}
	}
	
	private void register(){
		try {
			LogicMaster.registerMethodToRunEveryFrame(
				LevelFinish.class.getMethod("update", (Class<?>[]) null), this);
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(LevelFinish.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
