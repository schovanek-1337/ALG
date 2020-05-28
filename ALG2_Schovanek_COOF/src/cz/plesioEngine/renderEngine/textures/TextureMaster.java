package cz.plesioEngine.renderEngine.textures;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads textures, optimizes loading.
 * @author plesio
 */
public final class TextureMaster {

	private static final Map<String, Texture> loadedTextures = new HashMap<>();
	private static final Map<String, CubeMapTexture> loadedCubeMapTextures
		= new HashMap<>();

	private TextureMaster() {
	}

	public static Texture requestTexture(String name) {
		if (!loadedTextures.containsKey(name)) {
			Texture newTexture = new Texture("res/" + name + ".png");
			loadedTextures.put(name, newTexture);
			return newTexture;
		} else {
			return loadedTextures.get(name);
		}
	}

	public static Texture requestTexture(String name, int numberOfRows) {
		if (!loadedTextures.containsKey(name)) {
			Texture newTexture = new Texture("res/" + name + ".png");
			newTexture.setNumberOfRows(numberOfRows);
			loadedTextures.put(name, newTexture);
			return newTexture;
		} else {
			return loadedTextures.get(name);
		}
	}

	public static Texture requestTextureTiled(String name, float tileSize) {

		if (loadedTextures.containsKey(name)
			&& loadedTextures.get(name).getTileSize() == tileSize) {
			return loadedTextures.get(name);
		}

		Texture newTexture = new Texture("res/" + name + ".png");
		newTexture.setTileSize(tileSize);
		loadedTextures.put(name, newTexture);
		return newTexture;
	}

	public static CubeMapTexture requestCubeMapTexture(String key,
		String[] fileNames) {
		if (!loadedCubeMapTextures.containsKey(key)) {
			CubeMapTexture cubeMap = new CubeMapTexture(fileNames);
			loadedCubeMapTextures.put(key, cubeMap);
			return cubeMap;
		} else {
			return loadedCubeMapTextures.get(key);
		}
	}

	public static CubeMapTexture requestCubeMapTexture(String key) {
		if (loadedCubeMapTextures.containsKey(key)) {
			return loadedCubeMapTextures.get(key);
		}
		return null;
	}

}
