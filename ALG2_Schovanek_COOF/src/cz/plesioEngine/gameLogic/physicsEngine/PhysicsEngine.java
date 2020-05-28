package cz.plesioEngine.gameLogic.physicsEngine;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.PhysicsEntity;
import cz.plesioEngine.entities.enemies.Enemy;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.entities.staticEntities.MultimeshEntity;
import cz.plesioEngine.entities.weapons.WeaponEntity;
import cz.plesioEngine.gameLogic.levels.Room;
import cz.plesioEngine.renderEngine.particles.ParticleMaster;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.toolbox.MousePicker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.javatuples.Pair;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class PhysicsEngine {

	public static final float GRAVITY = 0.8f;

	private static List<WeaponEntity> weapons = new ArrayList<>();
	private static final List<Entity> physicsObjects = new ArrayList<>();
	private static List<PhysicsEntity> activeEntities = new ArrayList<>();

	/**
	 * Updates physics objects.
	 */
	public static void update() {
		updateWeaponTimers();
		ParticleMaster.update();
		updateGibs();
		Room.updateRooms();
	}

	private static void updateGibs() {
		List<PhysicsEntity> deadGibBatch = new ArrayList<>();
		for (PhysicsEntity g : activeEntities) {
			if (g.getIsDead()) {
				deadGibBatch.add(g);
				continue;
			}
			g.move();
		}
		activeEntities.removeAll(deadGibBatch);
	}

	public static void clear(){
		weapons.clear();
		physicsObjects.clear();
		activeEntities.clear();
	}
	
	/**
	 * Add physics to entities.
	 *
	 * @param g giblet.
	 */
	public static void processPhysicsEntity(PhysicsEntity g) {
		activeEntities.add(g);
	}

	/**
	 * Add entity to collision detection algorithms.
	 *
	 * @param e
	 */
	public static void processEntity(Entity e) {
		physicsObjects.add(e);
	}

	/**
	 * Add an entity with multiple meshes to collision detection.
	 *
	 * @param mme
	 */
	public static void processMultimeshEntity(MultimeshEntity mme) {
		for (Entity entity : mme.getEntitiesArray()) {
			processEntity(entity);
		}
	}

	private static void updateWeaponTimers() {
		for (WeaponEntity w : weapons) {
			w.setCurrentCooldown(w.getCurrentCooldown() - DisplayManager.getDelta());
			w.moveForward();
		}
	}

	/**
	 * For weapon timers, yeah idk what this is doing here.
	 *
	 * @param weapon weapon dude.
	 */
	public static void processWeapon(WeaponEntity weapon) {
		weapons.add(weapon);
	}

	public static Pair<Entity, Float> checkProjectileBodyCollision() {
		List<Pair<Entity, Float>> batch = new ArrayList<>();
		for (Entity e : physicsObjects) {
			float t = Collision.boundingBoxRayIntersection(e.getBoundingBox(),
				Camera.getPositionInstance(), MousePicker.getCurrentRay());
			if (t > -1) {
				batch.add(Pair.with(e, t));
			}
		}
		batch.sort((o1, o2) -> {
			return (int) (o1.getValue1() - o2.getValue1());
		});
		return batch.get(0);
	}

	public static List<Entity> getPhysicsObjects() {
		return Collections.unmodifiableList(physicsObjects);
	}

	public static void removeEntity(Entity e) {
		physicsObjects.remove(e);
	}

	/**
	 * Checks path for obstruction with a ray cast, returns the distance to
	 * the nearest object
	 *
	 * @param e enemy to test
	 * @param destination the normalized vector pointing towards destination
	 * @return distance to nearest intersection, empty pair if not found
	 */
	public static Pair<AABB, Float> checkEnemyPath(Enemy e, Vector3f destination) {
		List<Pair<Entity, Float>> batch = new ArrayList<>();
		for (Entity po : physicsObjects) {
			if (po.equals(e)) {
				continue;
			}
			float t = Collision.boundingBoxRayIntersection(po.getBoundingBox(),
				e.getPosition(), destination);
			if (t > 0) {
				batch.add(Pair.with(e, t));
			}
		}
		batch.sort((o1, o2) -> {
			return (int) (o1.getValue1() - o2.getValue1());
		});

		if (batch.isEmpty()) {
			return null;
		}

		return Pair.with(batch.get(0).getValue0().getBoundingBox(),
			batch.get(0).getValue1());
	}

	public static void checkPlayerMovement(Vector3f wishDirection, Vector3f positionReference) {
		for (Entity e : PhysicsEngine.getPhysicsObjects()) {
			if(e.getBoundingBox().equals(Camera.getBoundingBox())){
				continue;
			}
			if (Collision.boundingBoxIntersection(Camera.getBoundingBox(),
				e.getBoundingBox())) {

				Vector3f faceNormal = Collision.normalOfCollidedFace(
					Camera.getBoundingBox(), e.getBoundingBox());

				if (faceNormal.x == 1 && wishDirection.x < 0) {
					wishDirection.x = 0;
					Camera.decelerate();
				}
				if (faceNormal.x == -1 && wishDirection.x > 0) {
					wishDirection.x = 0;
					Camera.decelerate();
				}
				if (faceNormal.y == 1 && wishDirection.y < 0) {
					wishDirection.y = 0;
					if (Camera.getDirection().y() == 0) {
						Camera.setAlreadyJumped(false);
						Camera.decelerate(0.8f);
						positionReference.y
							= e.getBoundingBox().getMax()[1].getValue()
							+ 10.5f; // don't ask
					}
				}
				if (faceNormal.y == -1 && wishDirection.y > 0) {
					wishDirection.y = 0;
					Camera.decelerate();
				}
				if (faceNormal.z == 1 && wishDirection.z < 0) {
					wishDirection.z = 0;
					Camera.decelerate();
				}
				if (faceNormal.z == -1 && wishDirection.z > 0) {
					wishDirection.z = 0;
					Camera.decelerate();
				}
			}
		}
	}

	public static void checkEnemyMovement(Enemy enemy, Vector3f wishDirection) {
		for (Entity e : PhysicsEngine.getPhysicsObjects()) {
			if (e instanceof Enemy) {
				continue;
			}
			if (Collision.boundingBoxIntersection(enemy.getBoundingBox(),
				e.getBoundingBox())) {

				Vector3f faceNormal = Collision.normalOfCollidedFace(
					enemy.getBoundingBox(), e.getBoundingBox());

				if (faceNormal.x == 1 && wishDirection.x < 0) {
					wishDirection.x = 0;
					enemy.decelerate();
				}
				if (faceNormal.x == -1 && wishDirection.x > 0) {
					wishDirection.x = 0;
					enemy.decelerate();
				}
				if (faceNormal.y == 1 && wishDirection.y < 0) {
					wishDirection.y = 0;
					if (enemy.getVelocity().y() == 0) {
						enemy.decelerate(0.8f);
					}
				}
				if (faceNormal.y == -1 && wishDirection.y > 0) {
					wishDirection.y = 0;
					enemy.decelerate();
				}
				if (faceNormal.z == 1 && wishDirection.z < 0) {
					wishDirection.z = 0;
					enemy.decelerate();
				}
				if (faceNormal.z == -1 && wishDirection.z > 0) {
					wishDirection.z = 0;
					enemy.decelerate();
				}
			}
		}
	}

	public static void checkEntityMovement(PhysicsEntity e, Vector3f wishDirection) {
		for (Entity entity : PhysicsEngine.getPhysicsObjects()) {
			if (entity.equals(e)) {
				continue;
			}

			if (Collision.boundingBoxIntersection(e.getBoundingBox(),
				entity.getBoundingBox())) {

				Vector3f faceNormal = Collision.normalOfCollidedFace(
					e.getBoundingBox(), entity.getBoundingBox());

				if (faceNormal.x == 1 && wishDirection.x < 0) {
					wishDirection.x = 0;
					e.decelerate();
				}
				if (faceNormal.x == -1 && wishDirection.x > 0) {
					wishDirection.x = 0;
					e.decelerate();
				}
				if (faceNormal.y == 1 && wishDirection.y < 0) {
					wishDirection.y = 0;
					e.decelerate(0.3f);
				}
				if (faceNormal.y == -1 && wishDirection.y > 0) {
					wishDirection.y = 0;
					e.decelerate();
				}
				if (faceNormal.z == 1 && wishDirection.z < 0) {
					wishDirection.z = 0;
					e.decelerate();
				}
				if (faceNormal.z == -1 && wishDirection.z > 0) {
					wishDirection.z = 0;
					e.decelerate();
				}
			}
		}
	}

}
