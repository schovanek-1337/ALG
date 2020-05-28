package cz.plesioEngine.entities.enemies.AI;

import cz.plesioEngine.debugConsole.ConsoleOutput;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.gameLogic.physicsEngine.AABB;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.toolbox.FuzzyMaths;
import org.javatuples.Pair;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class EnemyMovement {

	private EnemyAI owner;
	private EnemyMovementState currentState = EnemyMovementState.IDLE;
	private Vector3f finalDestination;

	private static final float OBSTRUCTION_DISTANCE = 30;

	private static final float NODE_TIMEOUT = 1.5f;
	private float currentNodeTime = 0;

	private Vector3f currentNode;

	public EnemyMovement(EnemyAI owner) {
		this.owner = owner;
	}

	protected void move() {

		if (finalDestination == null) {
			ConsoleOutput.appendToLog("Enemy has no target",
				ConsoleOutput.LogType.ERR);
			return;
		}

		if ((currentNodeTime += DisplayManager.getDelta()) > NODE_TIMEOUT) {
			currentNode = null;
		}

		moveToNextNode();
		checkArrived();
	}

	public void setFinalDestination(Vector3f destination) {
		finalDestination = destination;
	}

	private Vector3f findNextNode() {

		if (!(currentState == EnemyMovementState.MOVING)) {
			return null;
		}

		Vector3f directionVector = new Vector3f(Camera.getPositionInstance()
			.sub(owner.getEnemy().getPosition())
			.normalize());

		Pair<AABB, Float> obstruction
			= PhysicsEngine.checkEnemyPath(owner.getEnemy(), directionVector);

		if (obstruction == null || obstruction.getValue1() > OBSTRUCTION_DISTANCE) {

			Vector3f nextNode = new Vector3f(owner.getEnemy().getPosition());
			nextNode.add(directionVector.mul(10));

			if (Camera.isAlreadyJumped()) {
				nextNode.y = owner.getEnemy().getPosition().y();
			}

			currentNodeTime = 0;

			return nextNode;
		} else {

			Vector3f dirVecCopy = new Vector3f();
			directionVector.round(dirVecCopy);

			Vector3f nextNode = new Vector3f(owner.getEnemy().getPosition());
			nextNode.add(directionVector.mul(10));

			currentNodeTime = 0;

			if (Camera.isAlreadyJumped()) {
				nextNode.y = owner.getEnemy().getPosition().y();
			}

			if (dirVecCopy.x == 1) {
				nextNode.rotateY((float) Math.toRadians(-60));
				return nextNode;
			}

			if (dirVecCopy.x == -1) {
				nextNode.rotateY((float) Math.toRadians(60));
				return nextNode;
			}

			if (dirVecCopy.z == 1) {
				nextNode.rotateY((float) Math.toRadians(60));
				return nextNode;
			}

			if (dirVecCopy.z == -1) {
				nextNode.rotateY((float) Math.toRadians(-60));
				return nextNode;
			}
		}
		return null;
	}

	private boolean moveToNextNode() {

		if (currentNode == null) {
			if ((currentNode = findNextNode()) == null) {
				currentState = EnemyMovementState.IDLE;
				return false;
			}
		}

		owner.getEnemy().lookAtNode(currentNode);

		owner.getEnemy().accelerate(owner.getEnemy().getForwardDirection(),
			owner.getEnemy().getSpeedCap(),
			owner.getEnemy().getAcceleration());

		if (FuzzyMaths.compareVectorsFuzzy(currentNode,
			owner.getEnemy().getPosition(), 8)) {
			currentNode = null;
		}

		return true;
	}

	private void checkArrived() {
		if (FuzzyMaths.compareVectorsFuzzy(owner.getEnemy().getPosition(),
			finalDestination, 10)) {
			currentState = EnemyMovementState.IDLE;
		}
	}

	public EnemyMovementState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(EnemyMovementState currentState) {
		this.currentState = currentState;
	}

}
