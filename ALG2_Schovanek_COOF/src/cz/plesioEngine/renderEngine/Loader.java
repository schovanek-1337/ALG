package cz.plesioEngine.renderEngine;

import cz.plesioEngine.gameLogic.physicsEngine.AABB;
import cz.plesioEngine.renderEngine.models.Mesh;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * For all OpenGL loading needs.
 * @author plesio
 */
public class Loader {

	private static final List<Integer> VAOS = new ArrayList<>();
	private static final List<Integer> VBOS = new ArrayList<>();
	private static final List<Integer> TEXTURES = new ArrayList<>();

	public static int loadBoundingBoxToVAO(AABB box) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, box.getVertices());
		unbindVAO();
		return vaoID;
	}

	private Loader() {
	}

	public static int loadMeshToVAO(Mesh mesh) {
		int vaoID = createVAO();
		bindIndicesBuffer(mesh.indices);
		storeDataInAttributeList(0, 3, mesh.vertices);
		storeDataInAttributeList(1, 3, mesh.textureCoordinates);
		storeDataInAttributeList(2, 3, mesh.normals);
		unbindVAO();
		return vaoID;
	}

	public static int loadGuiToVao(FloatBuffer positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		return vaoID;
	}

	public static int loadTextToVao(FloatBuffer positions, FloatBuffer textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	public static int loadCubeMapToVao(FloatBuffer positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return vaoID;
	}

	public static void cleanUp() {
		VAOS.forEach((vao) -> {
			glDeleteVertexArrays(vao);
		});
		VBOS.forEach((vbo) -> {
			glDeleteBuffers(vbo);
		});
		TEXTURES.forEach((texture) -> {
			GL15:
			glDeleteTextures(texture);
		});
	}

	private static int createVAO() {
		int vaoID = glGenVertexArrays();
		VAOS.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}

	private static void storeDataInAttributeList(int attributeNumber,
		int coordinateSize, FloatBuffer data) {
		int vboID = glGenBuffers();
		VBOS.add(vboID);
		data.rewind();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize,
			GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private static void bindIndicesBuffer(IntBuffer indices) {
		int vboID = glGenBuffers();
		VBOS.add(vboID);
		indices.rewind();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices,
			GL_STATIC_DRAW);
	}

	public static FloatBuffer floatArrayToFloatBuffer(float[] array) {
		FloatBuffer newBuffer = BufferUtils.createFloatBuffer(array.length);
		newBuffer.put(array);
		newBuffer.flip();
		return newBuffer;
	}

	public static int createEmptyVBO(int floatCount) {
		int vbo = glGenBuffers();
		VBOS.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, floatCount * 4, GL_STREAM_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	public static void addInstancedAttribute(int vao, int vbo, int attribute,
		int dataSize, int instancedDataLength, int offset) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindVertexArray(vao);
		glVertexAttribPointer(attribute, dataSize, GL_FLOAT, false,
			instancedDataLength * 4, offset * 4);
		glVertexAttribDivisor(attribute, 1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public static void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_STREAM_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private static void unbindVAO() {
		glBindVertexArray(0);
	}

	/**
	 * Registers a texture so it can remove it on OpenGL exit.
	 * @param textureID 
	 */
	public static void registerTexture(int textureID) {
		TEXTURES.add(textureID);
	}
}
