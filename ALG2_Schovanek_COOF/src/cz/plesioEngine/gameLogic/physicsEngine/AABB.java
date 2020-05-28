package cz.plesioEngine.gameLogic.physicsEngine;

import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.Loader;
import cz.plesioEngine.toolbox.Maths;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

/**
 * An axis aligned bounding box.
 * DEPRECATED.
 * @author plesio
 */
public final class AABB {

	private int vaoID;
	private Entity owner;
	private EndPoint[] min = new EndPoint[3];
	private EndPoint[] max = new EndPoint[3];
	private FloatBuffer vertices = BufferUtils.createFloatBuffer(108);

	/**
	 * This constructor generates an AABB on creation.
	 *
	 * @param entity
	 */
	public AABB(Entity entity) {
		owner = entity;
		generateBoundingBox(owner);
	}

	public void generateBoundingBox(Entity entity) {

		vertices.clear();

		Mesh mesh = entity.getTexturedModel().getMesh();

		float highestY = -Float.MAX_VALUE;
		float lowestY = Float.MAX_VALUE;

		float highestX = -Float.MAX_VALUE;
		float lowestX = Float.MAX_VALUE;

		float highestZ = -Float.MAX_VALUE;
		float lowestZ = Float.MAX_VALUE;

		Matrix4f transformationMatrix
			= Maths.createTransformationMatrix(
				new Vector3f(0, 0, 0),
				entity.getRotX(),
				entity.getRotY(),
				entity.getRotZ(),
				entity.getScale());

		float[] transformedVertices = new float[mesh.vertices.capacity()];

		for (int i = 0; i < transformedVertices.length; i += 3) {
			System.arraycopy(
				Maths.simpleMatrixVectorMultiply(
					mesh.vertices.get(i),
					mesh.vertices.get(i + 1),
					mesh.vertices.get(i + 2),
					transformationMatrix), 0, transformedVertices, i, 3);
		}

		//Determine peak points
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

		//Generate the box
		{
			//Top plane
			{
				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

			}
			//Leftmost plane
			{
				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(lowestZ);
			}
			//Back plane
			{
				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(lowestZ);
			}
			//Bottom plane
			{
				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(highestZ);
			}
			//Rightmost plane
			{
				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(highestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(highestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(highestZ);
			}
			//Front plane
			{
				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(lowestZ);

				vertices.put(lowestX);
				vertices.put(lowestY);
				vertices.put(highestZ);

				vertices.put(lowestX);
				vertices.put(highestY);
				vertices.put(highestZ);
			}
		}

		max[0] = new EndPoint(this, highestX, false);
		max[1] = new EndPoint(this, highestY, false);
		max[2] = new EndPoint(this, highestZ, false);

		min[0] = new EndPoint(this, lowestX, true);
		min[1] = new EndPoint(this, lowestY, true);
		min[2] = new EndPoint(this, lowestZ, true);

		this.vaoID = Loader.loadBoundingBoxToVAO(this);
		vertices.flip();
	}

	public void loadToVAO() {
		this.vaoID = Loader.loadBoundingBoxToVAO(this);
		vertices.flip();
	}

	/**
	 * Returns minimal endpoints tranformed with relation to owner's
	 * position.
	 *
	 * @return
	 */
	public EndPoint[] getMin() {

		EndPoint transformedValueX = new EndPoint(min[0]);
		transformedValueX.setValue(min[0].getValue() + owner.getPosition().x());

		EndPoint transformedValueY = new EndPoint(min[0]);
		transformedValueY.setValue(min[1].getValue() + owner.getPosition().y());

		EndPoint transformedValueZ = new EndPoint(min[0]);
		transformedValueZ.setValue(min[2].getValue() + owner.getPosition().z());

		EndPoint[] result = {transformedValueX,
			transformedValueY,
			transformedValueZ};

		return result;
	}

	/**
	 * Returns maximum endpoints tranformed with relation to owner's
	 * position.
	 *
	 * @return
	 */
	public EndPoint[] getMax() {
		EndPoint tranformedValueMinX = new EndPoint(max[0]);
		tranformedValueMinX.setValue(max[0].getValue() + owner.getPosition().x());

		EndPoint tranformedValueMinY = new EndPoint(max[0]);
		tranformedValueMinY.setValue(max[1].getValue() + owner.getPosition().y());

		EndPoint tranformedValueMinZ = new EndPoint(max[0]);
		tranformedValueMinZ.setValue(max[2].getValue() + owner.getPosition().z());

		EndPoint[] result = {tranformedValueMinX,
			tranformedValueMinY,
			tranformedValueMinZ};

		return result;
	}

	/**
	 * Vertex buffer containing rotated and scaled vertices ! THESE VERTICES
	 * AREN'T TRANSFORMED POSITIONALLY !
	 *
	 * @return
	 */
	public FloatBuffer getVertices() {
		return vertices;
	}

	/**
	 * Returns the owner
	 *
	 * @return
	 */
	public Entity getEntity() {
		return this.owner;
	}

	public int getVaoID() {
		return vaoID;
	}

}
