package cz.plesioEngine.renderEngine.shaders;

import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class StaticShader extends MetaShader {

	private static final String VERTEX_FILE
		= "/cz/plesioEngine/renderEngine/shaders/vertexShader.glsl";

	private static final String FRAGMENT_FILE
		= "/cz/plesioEngine/renderEngine/shaders/fragmentShader.glsl";

	private int location_useFakeLighting;

	private int location_numberOfRows;
	private int location_offset;
	private int location_ignoreViewMatrix;
	private int location_textureTileSize;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void getUncommonUniformLocations() {
		location_useFakeLighting
			= super.getUniformLocation("useFakeLighting");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_ignoreViewMatrix = super.getUniformLocation("ignoreViewMatrix");
		location_textureTileSize = super.getUniformLocation("textureTileSize");
	}

	public void loadTextureTileSize(float tileSize) {
		super.loadFloat(location_textureTileSize, tileSize);
	}

	public void loadIgnoreViewMatrix(boolean ignoreViewMatrix) {
		super.loadBoolean(location_ignoreViewMatrix, ignoreViewMatrix);
	}

	public void loadUseFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(location_useFakeLighting, useFakeLighting);
	}

	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	public void loadOffset(Vector2f offset) {
		super.loadVector2f(location_offset, offset);
	}

}
