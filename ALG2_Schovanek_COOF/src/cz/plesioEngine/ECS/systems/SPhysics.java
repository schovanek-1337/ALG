package cz.plesioEngine.ECS.systems;

import cz.plesioEngine.ECS.Entity;
import cz.plesioEngine.ECS.EntityManager;
import cz.plesioEngine.ECS.components.CAABB;
import cz.plesioEngine.ECS.components.CTexturedMesh;
import cz.plesioEngine.ECS.components.CTransform;
import cz.plesioEngine.ECS.systemComponents.EndPoint;
import cz.plesioEngine.debugConsole.ConsoleOutput;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.toolbox.Maths;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Physics component system.
 *
 * @author plesio
 */
public class SPhysics implements IComponentSystem {

	private List<Integer> activeEntities = new ArrayList<>();

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public void update() {

	}

	private void recalculateAABBs(int entityID) {

		Entity entity = EntityManager.getEntities().stream()
			.filter(e -> e.getID() == entityID)
			.findFirst()
			.orElse(null);

		if (entity == null) {
			return;
		}

		EntityManager.removeAllComponentsByID(CAABB.class, entity.getID());

		List<CTransform> transforms
			= EntityManager.getComponentList(CTransform.class);

		CTransform entityTransform = transforms.stream()
			.filter(transform -> transform.entityID == entityID)
			.findFirst()
			.orElse(null);

		if (entityTransform == null) {
			ConsoleOutput.appendToLog(
				"Attempted to create AABB without a transform, ID: "
				+ entityID,
				ConsoleOutput.LogType.ERR);
			return;
		}

		List<CTexturedMesh> texturedMeshes
			= EntityManager.getComponentList(CTexturedMesh.class);

		for (CTexturedMesh mesh : texturedMeshes) {

			float highestY = -Float.MAX_VALUE;
			float lowestY = Float.MAX_VALUE;

			float highestX = -Float.MAX_VALUE;
			float lowestX = Float.MAX_VALUE;

			float highestZ = -Float.MAX_VALUE;
			float lowestZ = Float.MAX_VALUE;

			Matrix4f transformationMatrix
				= Maths.createTransformationMatrix(
					new Vector3f(0, 0, 0),
					entityTransform.rotX,
					entityTransform.rotY,
					entityTransform.rotZ,
					entityTransform.scale);

			float[] transformedVertices
				= new float[mesh.mesh.vertices.capacity()];

			{
				for (int i = 0; i < transformedVertices.length; i += 3) {
					System.arraycopy(
						Maths.simpleMatrixVectorMultiply(
							mesh.mesh.vertices.get(i),
							mesh.mesh.vertices.get(i + 1),
							mesh.mesh.vertices.get(i + 2),
							transformationMatrix),
						0, transformedVertices, i, 3);
				}
			}

			{
				for (int i = 0; i < transformedVertices.length; i++) {

					if (i % 3 == 0) {
						if (transformedVertices[i] > highestX) {
							highestX = transformedVertices[i];
						}
						if (transformedVertices[i] < lowestX) {
							lowestX = transformedVertices[i];
						}
					}

					if ((i % 3) - 1 == 0) {
						if (transformedVertices[i] > highestY) {
							highestY = transformedVertices[i];
						}
						if (transformedVertices[i] < lowestY) {
							lowestY = transformedVertices[i];
						}
					}

					if ((i % 3) - 2 == 0) {
						if (transformedVertices[i] > highestZ) {
							highestZ = transformedVertices[i];
						}
						if (transformedVertices[i] < lowestZ) {
							lowestZ = transformedVertices[i];
						}
					}
				}
			}

			CAABB newCAABB = new CAABB();
			entity.addComponents(newCAABB);

			newCAABB.max[0] = new EndPoint(newCAABB, highestX, false);
			newCAABB.max[1] = new EndPoint(newCAABB, highestY, false);
			newCAABB.max[2] = new EndPoint(newCAABB, highestZ, false);

			newCAABB.min[0] = new EndPoint(newCAABB, lowestX, true);
			newCAABB.min[1] = new EndPoint(newCAABB, lowestY, true);
			newCAABB.min[2] = new EndPoint(newCAABB, lowestZ, true);

		}

	}

	private static void accelerate(Vector3f wishDirection, Vector3f velocity,
		float wishSpeed, float acceleration) {

		float currentSpeed = velocity.dot(wishDirection);
		float addspeed = wishSpeed - currentSpeed;

		if (addspeed <= 0) {
			return;
		}

		float accelspeed = acceleration * DisplayManager.getDelta() * wishSpeed;

		if (accelspeed > addspeed) {
			accelspeed = addspeed;
		}

		velocity.x += accelspeed * wishDirection.x;
		velocity.y += accelspeed * wishDirection.y;
		velocity.z += accelspeed * wishDirection.z;
	}

	private static void decelerate(Vector3f velocity, float amount) {
		velocity.mul(amount);
	}

	private int findHighestVarianceAxis() {
		return 0;
	}

}
