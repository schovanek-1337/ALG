package cz.plesioEngine.renderEngine.skyBox;

import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.renderEngine.textures.CubeMapTexture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author plesio
 */
public class SkyBoxRenderer {

	private static final float SIZE = 500f;

	private static final float[] VERTICES = {
		-SIZE, SIZE, -SIZE,
		-SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,
		-SIZE, -SIZE, SIZE,
		-SIZE, -SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,
		-SIZE, SIZE, SIZE,
		-SIZE, -SIZE, SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE, SIZE,
		-SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, -SIZE, SIZE,
		-SIZE, -SIZE, SIZE,
		-SIZE, SIZE, -SIZE,
		SIZE, SIZE, -SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		-SIZE, SIZE, SIZE,
		-SIZE, SIZE, -SIZE,
		-SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE, SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE, SIZE,
		SIZE, -SIZE, SIZE
	};

	private static final String[] TEXTURE_FILES = {"skybox/right", "skybox/left", "skybox/top",
		"skybox/bottom", "skybox/back", "skybox/front"};

	private static final String[] TEXTURE_FILES_NIGHT = {"skybox/nightRight", "skybox/nightLeft", "skybox/nightTop",
		"skybox/nightBottom", "skybox/nightBack", "skybox/nightFront"};

	private static Mesh cube;
	private static CubeMapTexture textureDay;
	private static CubeMapTexture textureNight;
	private static SkyBoxShader skyBoxShader;

	private static float blendFactor = 0f;

	public static void init(SkyBoxShader shader) {
		cube = MeshMaster.buildMesh(VERTICES, 3, "skybox");
		textureDay = TextureMaster.requestCubeMapTexture("skyboxDay", TEXTURE_FILES);
		textureNight = TextureMaster.requestCubeMapTexture("skyboxNight", TEXTURE_FILES_NIGHT);
		skyBoxShader = shader;
		skyBoxShader.start();
		skyBoxShader.connectTextureUnits();
		skyBoxShader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		skyBoxShader.stop();
	}

	public static void render() {

		if (!(GameStateMaster.getCurrentGameState() == GameState.PLAYING)) {
			return;
		}

		skyBoxShader.start();
		skyBoxShader.loadViewMatrix();
		skyBoxShader.loadFogColor(MasterRenderer.getSkyColor());
		GL30.glBindVertexArray(cube.vaoID);
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.vertices.capacity());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		skyBoxShader.stop();
	}

	private static void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureDay.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureNight.getTextureID());
		skyBoxShader.loadBlendFactor(blendFactor);
	}

	public static float getBlendFactor() {
		return blendFactor;
	}

	public static void setBlendFactor(float blendFactor) {
		SkyBoxRenderer.blendFactor = blendFactor;
	}

}
