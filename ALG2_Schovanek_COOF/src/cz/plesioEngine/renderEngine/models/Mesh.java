package cz.plesioEngine.renderEngine.models;

import cz.plesioEngine.renderEngine.Loader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

/**
 *
 * @author plesio
 */
public class Mesh {

	public int vaoID;

	public FloatBuffer vertices;
	public FloatBuffer textureCoordinates;
	public FloatBuffer normals;
	public IntBuffer indices;

	/**
	 * Represents a complete mesh with vertices, texture coordinates,
	 * normals and indices
	 *
	 * @param mesh AIMesh, from which the said attributes are extracted
	 */
	protected Mesh(AIMesh mesh) {
		processVertices(mesh);
		processIndices(mesh);
		processTextureCoordinates(mesh);
		processNormals(mesh);
		setVaoID();
	}

	/**
	 * Builds a mesh from separate arrays and loads it into VAO
	 *
	 * @param vertices
	 * @param normals
	 * @param textureCoords
	 * @param indices
	 */
	protected Mesh(float[] vertices,
		float[] normals, float[] textureCoords, int[] indices) {
		this.vertices = BufferUtils.createFloatBuffer(vertices.length);
		this.indices = BufferUtils.createIntBuffer(indices.length);
		this.textureCoordinates = BufferUtils.createFloatBuffer(textureCoords.length);
		this.normals = BufferUtils.createFloatBuffer(normals.length);

		this.vertices.put(vertices);
		this.indices.put(indices);
		this.textureCoordinates.put(textureCoords);
		this.normals.put(normals);
		setVaoID();
	}

	/**
	 * Build a simple mesh only from positions
	 *
	 * @param vertices
	 */
	protected Mesh(float[] vertices) {
		this.vertices = BufferUtils.createFloatBuffer(vertices.length);
		this.vertices.put(vertices);

		setSimpleVaoID();
	}

	/**
	 * Build a simple mesh only from positions and data size
	 *
	 * @param vertices
	 * @param dataSize
	 */
	protected Mesh(float[] vertices, int dataSize) {
		this.vertices = BufferUtils.createFloatBuffer(vertices.length);
		this.vertices.put(vertices);

		setSimpleVaoIDi(dataSize);
	}

	/**
	 * Reads vertices and writes them to the local FloatBuffer
	 *
	 * @param mesh
	 */
	private void processVertices(AIMesh mesh) {
		vertices = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 3);
		normals = BufferUtils.createFloatBuffer(mesh.mNumVertices());
		for (AIVector3D vertex : mesh.mVertices()) {
			vertices.put(vertex.x());
			vertices.put(vertex.y());
			vertices.put(vertex.z());
		}
		vertices.flip();
		normals.flip();
	}

	/**
	 * Reads indices and writes them to the local IntBuffer
	 *
	 * @param mesh
	 */
	private void processIndices(AIMesh mesh) {
		indices = BufferUtils.createIntBuffer(mesh.mNumFaces() * 3);
		for (AIFace face : mesh.mFaces()) {
			if (face.mNumIndices() == 3) {
				indices.put(face.mIndices());
			}
		}
		indices.flip();
	}

	/**
	 * Reads texture coordinates and writes them to the local FloatBuffer
	 *
	 * @param mesh
	 */
	private void processTextureCoordinates(AIMesh mesh) {
		textureCoordinates = BufferUtils.createFloatBuffer(
			mesh.mTextureCoords(0).capacity() * 3);
		for (AIVector3D textureCoord : mesh.mTextureCoords(0)) {
			textureCoordinates.put(textureCoord.x());
			textureCoordinates.put(textureCoord.y());
			textureCoordinates.put(textureCoord.z());
		}
		textureCoordinates.flip();
	}

	private void processNormals(AIMesh mesh) {
		normals = BufferUtils.createFloatBuffer(mesh.mNormals().capacity() * 3);
		for (AIVector3D normal : mesh.mNormals()) {
			normals.put(normal.x());
			normals.put(normal.y());
			normals.put(normal.z());
		}
		normals.flip();
	}

	/**
	 * Load the mesh into a VAO, set its ID
	 */
	private void setVaoID() {
		this.vaoID = Loader.loadMeshToVAO(this);
	}

	/**
	 * Only for use when a mesh is made only from vertices, used for GUI
	 */
	private void setSimpleVaoID() {
		this.vaoID = Loader.loadGuiToVao(vertices);
	}

	/**
	 * Used for loading cubeMaps into VAOs
	 *
	 * @param dataSize
	 */
	private void setSimpleVaoIDi(int dataSize) {
		this.vaoID = Loader.loadCubeMapToVao(vertices, dataSize);
	}

}
