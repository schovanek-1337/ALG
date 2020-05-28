package cz.plesioEngine.renderEngine.models;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author plesio
 */
public final class MeshMaster {

	private static final Map<String, Mesh> loadedMeshes = new HashMap<>();

	private MeshMaster() {
	}

	/**
	 * Loads a mesh from an .obj file.
	 * @param name path to the file.
	 * @return a mesh.
	 */
	public static Mesh requestMesh(String name) {
		if (!loadedMeshes.containsKey(name)) {
			Mesh[] newMesh = null;

			try {
				newMesh = StaticMeshLoader.load("res/" + name + ".obj");
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}

			loadedMeshes.put(name, newMesh[0]);
			return newMesh[0];
		} else {
			return loadedMeshes.get(name);
		}
	}

	/**
	 * Loads multiple meshes from an .obj file.
	 * @param path path to the file. ("res/PATH/.obj"
	 * @return mesh array.
	 */
	public static Mesh[] requestMeshGroup(String path) {

		Mesh[] meshes = null;

		try {
			meshes = StaticMeshLoader.load("res/" + path + ".obj");
		} catch (Exception ex) {
			Logger.getLogger(MeshMaster.class.getName()).log(Level.SEVERE, null, ex);
		}

		return meshes;
	}

	/**
	 * Builds a mesh from its base elements.
	 * @param vertices
	 * @param normals
	 * @param textureCoords
	 * @param indices
	 * @param name
	 * @return 
	 */
	public static Mesh buildMesh(float[] vertices,
		float[] normals, float[] textureCoords, int[] indices, String name) {

		Mesh newMesh = new Mesh(vertices, normals, textureCoords, indices);
		loadedMeshes.put(name, newMesh);
		return newMesh;
	}

	/**
	 * Builds a vertex-only mesh, used for GUIs.
	 * @param vertices
	 * @param name
	 * @return 
	 */
	public static Mesh buildMesh(float[] vertices, String name) {
		Mesh newMesh = new Mesh(vertices);
		loadedMeshes.put(name, newMesh);
		return newMesh;
	}

	/**
	 * Builds a vertex-only mesh. Used for skyboxes.
	 * @param vertices
	 * @param dataSize
	 * @param name
	 * @return 
	 */
	public static Mesh buildMesh(float[] vertices, int dataSize, String name) {
		Mesh newMesh = new Mesh(vertices, dataSize);
		loadedMeshes.put(name, newMesh);
		return newMesh;
	}

}
