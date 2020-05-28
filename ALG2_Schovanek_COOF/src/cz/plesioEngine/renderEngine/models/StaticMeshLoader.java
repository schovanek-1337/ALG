package cz.plesioEngine.renderEngine.models;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import static org.lwjgl.assimp.Assimp.*;

/**
 *
 * @author plesio
 */
public class StaticMeshLoader {

	protected static Mesh[] load(String resourcePath) throws Exception {

		return load(resourcePath, aiProcess_JoinIdenticalVertices
			| aiProcess_Triangulate | aiProcess_FixInfacingNormals);
	}

	protected static Mesh[] load(String resourcePath, int flags)
		throws Exception {

		AIScene aiScene = aiImportFile(resourcePath, flags);
		if (aiScene == null) {
			throw new Exception("Error loading model" + " " + resourcePath);
		}

		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		Mesh[] meshes = new Mesh[numMeshes];
		for (int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			Mesh mesh = new Mesh(aiMesh);
			meshes[i] = mesh;
		}

		return meshes;
	}

}
