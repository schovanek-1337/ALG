package cz.plesioEngine.gameLogic.levels;

import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.toolbox.Maths;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * UNUSED.
 * @author plesio
 */
public class StructureGenerator {

	public static Mesh generatePlane(float height, float width, float rotX,
		float rotY, float rotZ, String name) {

		Vector3f vertexVector1 = new Vector3f(0, 0, 0);
		Vector3f vertexVector2 = new Vector3f(-width, 0, -height);
		Vector3f vertexVector3 = new Vector3f(-width, 0, 0);
		Vector3f vertexVector4 = new Vector3f(0, 0, -height);

		float[] vertices = new float[12];

		float[] textureCoords = {0, 1, 0,
			1, 0, 0,
			1, 1, 0,
			0, 0, 0
		};

		float[] normals = {0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f};

		int[] indices = {
			0, 1, 2,
			0, 3, 1
		};

		Matrix4f transformationMatrix
			= Maths.createTransformationMatrix(new Vector3f(0, 0, 0), rotX,
				rotY, rotZ, 1);

		Matrix3f reducedTransformationMatrix = new Matrix3f();
		transformationMatrix.get3x3(reducedTransformationMatrix);

		vertexVector1.mul(reducedTransformationMatrix);
		vertexVector2.mul(reducedTransformationMatrix);
		vertexVector3.mul(reducedTransformationMatrix);
		vertexVector4.mul(reducedTransformationMatrix);

		vertices[0] = vertexVector1.x;
		vertices[1] = vertexVector1.y;
		vertices[2] = vertexVector1.z;
		vertices[3] = vertexVector2.x;
		vertices[4] = vertexVector2.y;
		vertices[5] = vertexVector2.z;
		vertices[6] = vertexVector3.x;
		vertices[7] = vertexVector3.y;
		vertices[8] = vertexVector3.z;
		vertices[9] = vertexVector4.x;
		vertices[10] = vertexVector4.y;
		vertices[11] = vertexVector4.z;

		Mesh finalMesh = MeshMaster.buildMesh(vertices, normals, textureCoords,
			indices, name);

		return finalMesh;
	}

}
