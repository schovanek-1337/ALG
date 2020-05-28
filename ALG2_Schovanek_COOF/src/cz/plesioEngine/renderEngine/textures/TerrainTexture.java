package cz.plesioEngine.renderEngine.textures;

/**
 *
 * @author plesio
 */
public class TerrainTexture {

	private final int textureID;
	private final Texture texture;

	public TerrainTexture(Texture texture) {
		this.textureID = texture.getTextureID();
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public int getTextureID() {
		return textureID;
	}

}
