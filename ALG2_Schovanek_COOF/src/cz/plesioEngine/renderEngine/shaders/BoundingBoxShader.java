package cz.plesioEngine.renderEngine.shaders;

import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.toolbox.Maths;
import org.joml.Matrix4f;

/**
 *
 * @author plesio
 */
public class BoundingBoxShader extends ShaderProgram {

	private static final String VERTEX_FILE
		= "/cz/plesioEngine/renderEngine/shaders/boundingBoxVertexShader.glsl";

	private static final String FRAGMENT_FILE
		= "/cz/plesioEngine/renderEngine/shaders/boundingBoxFragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public BoundingBoxShader() {
		//super(VERTEX_FILE, FRAGMENT_FILE, GEOMETRY_FILE);
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getCommonUniformLocations() {
		location_transformationMatrix
			= super.getUniformLocation("transformationMatrix");
		location_projectionMatrix
			= super.getUniformLocation("projectionMatrix");
		location_viewMatrix
			= super.getUniformLocation("viewMatrix");
	}

	public void loadViewMatrix() {
		super.loadMatrix(location_viewMatrix, Maths.createViewMatrix());
	}

	public void loadProjectionMatrix() {
		super.loadMatrix(location_projectionMatrix,
			MasterRenderer.getProjectionMatrix());
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

}
