package cz.plesioEngine.guis.fontRendering;

import cz.plesioEngine.renderEngine.shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE
		= "/cz/plesioEngine/guis/fontRendering/fontVertex.glsl";
	private static final String FRAGMENT_FILE
		= "/cz/plesioEngine/guis/fontRendering/fontFragment.glsl";

	private int location_color;
	private int location_translation;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getCommonUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
	}

	protected void loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}

	protected void loadTranslation(Vector2f translation) {
		super.loadVector2f(location_translation, translation);
	}

}
