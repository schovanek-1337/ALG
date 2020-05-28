package cz.plesioEngine.gameLogic.levels;

import cz.plesioEngine.entities.enemies.Enemy;
import cz.plesioEngine.entities.weapons.WeaponMaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author plesio
 */
public class EnemySpawn {

	private int counter = 0;
	private EnemySpawnType spawnType = EnemySpawnType.QUEUE;
	private List<Enemy> enemiesToSpawn = new ArrayList<>();
	private List<Enemy> currentEnemies = new ArrayList<>();
	private boolean canEmit = true;
	private boolean finished = false;
	private boolean waitingForNextWave = false;
	private int maxNumberOfWaves = 1;
	private int currentWave = 1;

	/**
	 * Emits an enemy based on the type of spawn
	 */
	public void emit() {

		List<Enemy> deadEnemies = new ArrayList<>();
		for (Enemy e : currentEnemies) {
			if(e.getPosition().y() < -10){
				e.modifyHealth(-1000);
			}
			if (!e.isAlive()) {
				deadEnemies.add(e);
				e.unregister();
				//Vector3f gibPosition = new Vector3f(e.getPosition());
				//gibPosition.y += 10;
				//e.getGibEmmiter().emitGiblets(gibPosition);
				WeaponMaster.getHeldWeapon().addAmmoRand(5);
			}
		}

		currentEnemies.removeAll(deadEnemies);

		if (!canEmit) {
			return;
		}

		switch (spawnType) {
			case QUEUE:
				if (currentEnemies.isEmpty()) {
					if (counter >= enemiesToSpawn.size()) {
						finished = true;
						if (currentWave < maxNumberOfWaves) {
							counter = 0;
							canEmit = false;
							finished = false;
							waitingForNextWave = true;
						} else {
							return;
						}
					}
					enemiesToSpawn.get(counter).register();
					currentEnemies.add(enemiesToSpawn.get(counter));
					counter++;
				}
		}
	}

	/**
	 * Checks if all provided spawns are waiting for the next wave.
	 * @param spawns
	 * @return 
	 */
	public static boolean allWaitingForNextWave(List<EnemySpawn> spawns) {
		for (EnemySpawn esp : spawns) {
			if (!esp.getWaitingForNextWave()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if all provided spawn have finished spawning.
	 * @param spawns
	 * @return 
	 */
	public static boolean allFinished(List<EnemySpawn> spawns) {
		for (EnemySpawn esp : spawns) {
			if (!esp.isFinished()) {
				return false;
			}
		}
		return true;
	}

	public void addEnemyToSpawn(Enemy... enemies) {
		for (Enemy e : enemies) {
			Enemy newEnemy = new Enemy(e);
			enemiesToSpawn.add(newEnemy);
		}

	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isCanEmit() {
		return canEmit;
	}

	public void setCanEmit(boolean canEmit) {
		this.canEmit = canEmit;
	}

	public void resetWaitForNextWave() {
		this.waitingForNextWave = true;
	}

	public boolean getWaitingForNextWave() {
		return waitingForNextWave;
	}

}
