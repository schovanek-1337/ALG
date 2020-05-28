package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.renderEngine.Loader;
import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.toolbox.Maths;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

/**
 *
 * @author plesio
 */
public final class ParticleRenderer {

	private static ParticleShader particleShader;

	private static int particleVBO;
	private static int particleVAO;

	private static final float[] VERTICES = {-0.5f, 0.5f,
		-0.5f, -0.5f,
		0.5f, 0.5f,
		0.5f, -0.5f};
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 21;

	private static final FloatBuffer buffer
		= BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	private static int pointer;

	private ParticleRenderer() {

	}

	protected static void init(ParticleShader shader) {
		particleVAO = Loader.loadGuiToVao(
			Loader.floatArrayToFloatBuffer(VERTICES));
		particleShader = shader;
		particleShader.start();
		particleShader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		particleShader.stop();

		particleVBO = Loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		Loader.addInstancedAttribute(particleVAO, particleVBO, 1, 4, INSTANCE_DATA_LENGTH, 0);
		Loader.addInstancedAttribute(particleVAO, particleVBO, 2, 4, INSTANCE_DATA_LENGTH, 4);
		Loader.addInstancedAttribute(particleVAO, particleVBO, 3, 4, INSTANCE_DATA_LENGTH, 8);
		Loader.addInstancedAttribute(particleVAO, particleVBO, 4, 4, INSTANCE_DATA_LENGTH, 12);
		Loader.addInstancedAttribute(particleVAO, particleVBO, 5, 4, INSTANCE_DATA_LENGTH, 16);
		Loader.addInstancedAttribute(particleVAO, particleVBO, 6, 1, INSTANCE_DATA_LENGTH, 20);
	}

	protected static void render(Map<Texture, List<Particle>> particles) {
		prepare();
		for (Texture texture : particles.keySet()) {
			bindTexture(texture);
			List<Particle> particleList = particles.get(texture);
			pointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			boolean dead = true;
			for (Particle particle : particleList) {
				if (!particle.getIsDead()) {
					dead = false;
					updateParticleModelViewMatrix(particle.getPosition(),
						particle.getRotation(), particle.getScale(), vboData);
					updateTexCoordInfo(particle, vboData);
				}
			}
			if (!dead) {
				Loader.updateVBO(particleVBO, vboData, buffer);
				GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0,
					VERTICES.length, particleList.size());
			}
		}
		finishRender();
	}

	private static void bindTexture(Texture texture) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		particleShader.loadNumberOfRows(texture.getNumberOfRows());
	}

	private static void updateTexCoordInfo(Particle particle, float[] data) {
		data[pointer++] = particle.getTexOffset1().x;
		data[pointer++] = particle.getTexOffset1().y;
		data[pointer++] = particle.getTexOffset2().x;
		data[pointer++] = particle.getTexOffset2().y;
		data[pointer++] = particle.getBlend();
	}

	private static void updateParticleModelViewMatrix(Vector3f position,
		float rotation, float scale, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f viewMatrix = Maths.createViewMatrix();
		modelMatrix.translate(position);
		viewMatrix.transpose3x3(modelMatrix);
		modelMatrix.rotate(rotation, new Vector3f(0, 0, 1));
		modelMatrix.scale(scale);
		Matrix4f modelViewMatrix = viewMatrix.mul(modelMatrix);
		storeMatrixData(modelViewMatrix, vboData);
	}

	private static void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00();
		vboData[pointer++] = matrix.m01();
		vboData[pointer++] = matrix.m02();
		vboData[pointer++] = matrix.m03();
		vboData[pointer++] = matrix.m10();
		vboData[pointer++] = matrix.m11();
		vboData[pointer++] = matrix.m12();
		vboData[pointer++] = matrix.m13();
		vboData[pointer++] = matrix.m20();
		vboData[pointer++] = matrix.m21();
		vboData[pointer++] = matrix.m22();
		vboData[pointer++] = matrix.m23();
		vboData[pointer++] = matrix.m30();
		vboData[pointer++] = matrix.m31();
		vboData[pointer++] = matrix.m32();
		vboData[pointer++] = matrix.m33();
	}

	private static void prepare() {
		particleShader.start();
		GL30.glBindVertexArray(particleVAO);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}

	private static void finishRender() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL30.glBindVertexArray(0);
		particleShader.stop();
	}

	protected static void cleanUp() {
		particleShader.cleanUp();
	}

}
