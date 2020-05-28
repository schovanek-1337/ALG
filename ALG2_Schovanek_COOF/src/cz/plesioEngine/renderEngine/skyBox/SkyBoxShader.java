package cz.plesioEngine.renderEngine.skyBox;

import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.shaders.ShaderProgram;
import cz.plesioEngine.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class SkyBoxShader extends ShaderProgram {

	private static final String VERTEX_FILE
		= "/cz/plesioEngine/renderEngine/skyBox/skyBoxVertexShader.glsl";
	private static final String FRAGMENT_FILE
		= "/cz/plesioEngine/renderEngine/skyBox/skyBoxFragmentShader.glsl";

	private static float rotationSpeed = 0.08f;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_blendFactor;
	private int location_cubeMapDay;
	private int location_cubeMapNight;

	private float rotation = 0;

	public SkyBoxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix() {
		Matrix4f matrix = Maths.createViewMatrix();
		matrix._m30(0);
		matrix._m31(0);
		matrix._m32(0);
		rotation += rotationSpeed * DisplayManager.getDelta();
		matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0));
		super.loadMatrix(location_viewMatrix, matrix);
	}

	@Override
	protected void getCommonUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_cubeMapNight = super.getUniformLocation("cubeMapNight");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_cubeMapDay = super.getUniformLocation("cubeMapDay");
		location_fogColor = super.getUniformLocation("fogColor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void connectTextureUnits() {
		super.loadInt(location_cubeMapDay, 0);
		super.loadInt(location_cubeMapNight, 1);
	}

	public void loadFogColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r, g, b));
	}

	public void loadFogColor(Vector3f color) {
		super.loadVector(location_fogColor, color);
	}

	public void loadBlendFactor(float factor) {
		super.loadFloat(location_blendFactor, factor);
	}

	public static float getRotationSpeed() {
		return rotationSpeed;
	}

	public static void setRotationSpeed(float rotationSpeed) {
		SkyBoxShader.rotationSpeed = rotationSpeed;
	}

}
