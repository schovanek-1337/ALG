package cz.plesioEngine.entities.enemies.AI;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.enemies.Enemy;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author plesio
 */
public final class AIMaster {

	private static List<Enemy> enemyList = new ArrayList<>();

	private AIMaster() {

	}

	public static void update() {
		if (GameStateMaster.getCurrentGameState() == GameState.PLAYING) {
			for (Enemy enemy : enemyList) {
				enemy.update();
				enemy.getAI().run();
			}
		}
	}

	public static void processEnemy(Enemy e) {
		enemyList.add(e);
		e.getAI().getMovementSystem().setFinalDestination(Camera.getPositionReference());
	}

	public static void removeEnemy(Enemy e) {
		enemyList.remove(e);
	}
	
	public static void clear(){
		enemyList.clear();
	}

}
