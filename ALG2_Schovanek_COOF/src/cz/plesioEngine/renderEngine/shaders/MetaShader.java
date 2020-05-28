package cz.plesioEngine.renderEngine.shaders;

import cz.plesioEngine.entities.lights.LightMaster;
import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
@SuppressWarnings("ProtectedField")
public abstract class MetaShader extends ShaderProgram {

	protected int location_transformationMatrix;
	protected int location_projectionMatrix;
	protected int location_viewMatrix;

	protected int[] location_lightPosition;
	protected int[] location_lightColor;

	protected int[] location_attenuation;

	protected int location_shineDamper;
	protected int location_reflectivity;

	protected int location_skyColor;
	
	protected int location_glow;

	public MetaShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getCommonUniformLocations() {
		location_transformationMatrix
			= super.getUniformLocation("transformationMatrix");
		location_projectionMatrix
			= super.getUniformLocation("projectionMatrix");
		location_viewMatrix
			= super.getUniformLocation("viewMatrix");
		location_shineDamper
			= super.getUniformLocation("shineDamper");
		location_reflectivity
			= super.getUniformLocation("reflectivity");
		location_skyColor
			= super.getUniformLocation("skyColor");
		location_glow
			= super.getUniformLocation("glow");

		location_attenuation = new int[LightMaster.LIGHT_LIMIT];
		location_lightPosition = new int[LightMaster.LIGHT_LIMIT];
		location_lightColor = new int[LightMaster.LIGHT_LIMIT];
		for (int i = 0; i < LightMaster.LIGHT_LIMIT; i++) {
			location_lightPosition[i]
				= super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i]
				= super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i]
				= super.getUniformLocation("attenuation[" + i + "]");
		}

		getUncommonUniformLocations();
	}

	protected abstract void getUncommonUniformLocations();

	public void loadGlow(boolean glow){
		super.loadBoolean(location_glow, glow);
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

	public void loadDynamicLights() {
		int dynamicLightsSize = LightMaster.getDynamicLights().size();
		int staticLightsSize = LightMaster.getStaticLights().size();

		for (int i = staticLightsSize; i < LightMaster.LIGHT_LIMIT; i++) {
			if ((i - staticLightsSize) > (dynamicLightsSize - 1)) {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			} else {
				super.loadVector(location_lightPosition[i],
					LightMaster.getDynamicLights().get(i
						- staticLightsSize).getPosition());

				super.loadVector(location_lightColor[i],
					LightMaster.getDynamicLights().get(i
						- staticLightsSize).getColor());

				super.loadVector(location_attenuation[i],
					LightMaster.getDynamicLights().get(i
						- staticLightsSize).getAttenuation());
			}
		}
	}

	public void loadStaticLights() {
		for (int i = 0; i < LightMaster.getStaticLights().size(); i++) {
			super.loadVector(location_lightPosition[i],
				LightMaster.getStaticLights().get(i).getPosition());
			super.loadVector(location_lightColor[i],
				LightMaster.getStaticLights().get(i).getColor());
			super.loadVector(location_attenuation[i],
				LightMaster.getStaticLights().get(i).getAttenuation());
		}
	}

	public void loadShineVariables(float reflectivity, float shineDamper) {
		super.loadFloat(location_reflectivity, reflectivity);
		super.loadFloat(location_shineDamper, shineDamper);
	}

	public void loadSkyColor(float red, float green, float blue) {
		super.loadVector(location_skyColor, new Vector3f(red, green, blue));
	}

}
