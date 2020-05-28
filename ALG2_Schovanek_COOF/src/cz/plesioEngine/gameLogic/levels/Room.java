package cz.plesioEngine.gameLogic.levels;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.entities.staticEntities.ToiletPaper;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.toolbox.FuzzyMaths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Room {

	private RoomState currentState = RoomState.READY;

	private List<Vector3f> entryPoints = new ArrayList<>();

	private List<Door> doors = new ArrayList<>();

	private List<EnemySpawn> spawns = new ArrayList<>();

	private Entity goal;
	private ToiletPaper toiletPaper;

	private static List<Room> rooms = new ArrayList<>();

	public Room(Vector3f... entryPoints) {
		this.entryPoints.addAll(Arrays.asList(entryPoints));
		addRoom();
	}

	public static void updateRooms() {
		for (Room r : rooms) {
			if (r.playerIsInside() && r.getState() == RoomState.READY) {
				r.setRoomState(RoomState.COMBAT);
			}

			if (r.getState() == RoomState.COMBAT) {
				for (Door d : r.getDoors()) {
					d.activateDoor(false);
				}
				if (EnemySpawn.allWaitingForNextWave(r.getSpawns())) {
					for (EnemySpawn s : r.getSpawns()) {
						s.resetWaitForNextWave();
						s.setCanEmit(true);
					}
				} else {
					for (EnemySpawn s : r.getSpawns()) {
						if (!s.isFinished()) {
							s.emit();
						}
					}
					if (EnemySpawn.allFinished(r.getSpawns())) {
						r.setRoomState(RoomState.CLEARED);
						r.getGoal().getPosition().add(0, 50, 0);
					}
				}
			}

			if (r.getState() == RoomState.CLEARED) {
				if (FuzzyMaths.compareVectorsFuzzy(
					r.getToiletPaper().getPosition(),
					Camera.getPositionInstance(), 15)) {
					for (Door d : r.getDoors()) {
						d.activateDoor(true);
					}
					EntityRenderer.removeEntity(r.getToiletPaper());
					PhysicsEngine.removeEntity(r.getToiletPaper());
				}
			}

		}
		//check if player is inside room
		//spawn enemies
		//check if he picked up the toilet paper
	}
	
	public static void removeRooms(){
		rooms.clear();
	}

	private boolean playerIsInside() {
		for (Vector3f entrance : entryPoints) {
			if (FuzzyMaths.compareVectorsFuzzy(entrance,
				Camera.getPositionInstance(), 50)) {
				return true;
			}
		}
		return false;
	}

	public void setToiletPaper(ToiletPaper tp) {
		this.toiletPaper = tp;
	}

	public ToiletPaper getToiletPaper() {
		return toiletPaper;
	}

	public void addDoor(Door d) {
		doors.add(d);
	}

	private void addRoom() {
		rooms.add(this);
	}

	private RoomState getState() {
		return currentState;
	}

	private void setRoomState(RoomState state) {
		this.currentState = state;
	}

	public List<EnemySpawn> getSpawns() {
		return spawns;
	}

	public List<Door> getDoors() {
		return this.doors;
	}

	public Entity getGoal() {
		return goal;
	}

	public void setGoal(Entity e) {
		this.goal = e;
	}

	public void addEnemySpawn(EnemySpawn esp) {
		spawns.add(esp);
	}

}
