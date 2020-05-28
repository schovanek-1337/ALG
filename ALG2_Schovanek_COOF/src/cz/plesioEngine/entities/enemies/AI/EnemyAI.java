package cz.plesioEngine.entities.enemies.AI;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.enemies.Enemy;
import cz.plesioEngine.gameLogic.SettingMaster;
import cz.plesioEngine.toolbox.Maths;

/**
 *
 * @author plesio
 */
public class EnemyAI {

	private Enemy governedEnemy;

	private EnemyMovement movementSystem;
	private EnemyDamage damageSystem;

	public EnemyAI(Enemy enemy) {
		this.governedEnemy = enemy;
		this.movementSystem = new EnemyMovement(this);
		this.damageSystem = new EnemyDamage();
	}

	public Enemy getEnemy() {
		return governedEnemy;
	}

	public void run() {
		if(!SettingMaster.enableAI) return;
		movementSystem.move();
		checkShouldChasePlayer();
		damageSystem.updatePosition(governedEnemy.getPosition());
		damageSystem.update();
	}

	private boolean checkShouldChasePlayer() {
		if (movementSystem.getCurrentState() == EnemyMovementState.IDLE
			&& Maths.isInside(governedEnemy.getPosition().x(),
				governedEnemy.getPosition().z(),
				Camera.getPositionInstance().x(),
				Camera.getPositionInstance().z(),
				300)) {
			movementSystem.setCurrentState(EnemyMovementState.MOVING);
		}
		return false;
	}

	public EnemyMovement getMovementSystem() {
		return movementSystem;
	}

}
