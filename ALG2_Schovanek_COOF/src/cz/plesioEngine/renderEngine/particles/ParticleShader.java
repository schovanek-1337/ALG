package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;

/**
 * Clean and simple.
 *
 * @author plesio
 */
public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE
		= "/cz/plesioEngine/renderEngine/particles/vertexShader.glsl";
	private static final String FRAGMENT_FILE
		= "/cz/plesioEngine/renderEngine/particles/fragmentShader.glsl";

	private int location_numberOfRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

	protected void loadNumberOfRows(float numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	@Override
	protected void getCommonUniformLocations() {
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffset");
		super.bindAttribute(6, "blendFactor");
	}

}
