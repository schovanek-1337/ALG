package cz.plesioEngine.guis;

import cz.plesioEngine.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE
		= "/cz/plesioEngine/guis/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE
		= "/cz/plesioEngine/guis/guiFragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_transparency;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadTrasparency(float amount) {
		super.loadFloat(location_transparency, amount);
	}

	@Override
	protected void getCommonUniformLocations() {
		location_transformationMatrix
			= super.getUniformLocation("transformationMatrix");
		location_transparency
			= super.getUniformLocation("transparency");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
