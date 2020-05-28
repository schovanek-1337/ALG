package cz.plesioEngine.entities.enemies.AI;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.toolbox.FuzzyMaths;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
class EnemyDamage {
	
	private int value = -13;
	
	private float damageCooldown = 0.5f;
	private float damageCooldownCurrent = 0f;
	
	private Vector3f positionReference;
	
	public void updatePosition(Vector3f position){
		positionReference = position;
	}
	
	public void update(){
		if(damageCooldownCurrent > 0){
			damageCooldownCurrent -= DisplayManager.getDelta();
		}else {
			if(FuzzyMaths.compareVectorsFuzzy(
				Camera.getPositionInstance(), 
				positionReference, 15)){
				PlayerStats.health.changeHealth(value);
				damageCooldownCurrent = damageCooldown;
			}
		}
	}
	
}
