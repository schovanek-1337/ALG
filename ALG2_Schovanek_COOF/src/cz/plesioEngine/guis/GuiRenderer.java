package cz.plesioEngine.guis;

import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.toolbox.Maths;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author plesio
 */
public class GuiRenderer {

	private static Mesh quad;

	private static final List<GuiTexture> guis = new ArrayList<>();

	private static GuiShader guiShader;

	private GuiRenderer() {
	}

	public static void init(GuiShader shader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = MeshMaster.buildMesh(positions, "GuiElement");
		guiShader = shader;
	}

	public static void processGUI(GuiTexture gui) {
		if (!guis.contains(gui)) {
			guis.add(gui);
		}
	}

	public static void removeGUI(GuiTexture gui) {
		guis.remove(gui);
	}
	
	private static void finish(){
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void render() {
		guiShader.start();
		GL30.glBindVertexArray(quad.vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture texture : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,
				texture.getTexture().getTextureID());

			Matrix4f transformationMatrix
				= Maths.createTransformationMatrix(texture.getPosition(),
					texture.getScale());
			guiShader.loadTransformation(transformationMatrix);
			guiShader.loadTrasparency(texture.getTransparency());

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,
				0, quad.vertices.capacity());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		finish();
	}
	
	public static void clear(){
		guis.clear();
	}

}
