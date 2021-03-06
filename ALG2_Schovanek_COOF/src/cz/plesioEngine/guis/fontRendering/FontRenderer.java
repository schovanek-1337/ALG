package cz.plesioEngine.guis.fontRendering;

import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class FontRenderer {

	private static FontShader fontShader;

	public static void init(FontShader shader) {
		fontShader = shader;
	}

	private FontRenderer() {
	}

	protected static void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_2D, font.getTextureAtlas());
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}

	private static void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		fontShader.start();
	}

	private static void renderText(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		fontShader.loadColor(text.getColor());
		fontShader.loadTranslation(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	private static void endRendering() {
		fontShader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
