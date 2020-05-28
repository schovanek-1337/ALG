package cz.plesioEngine.renderEngine.models;

import cz.plesioEngine.renderEngine.textures.Texture;

/**
 *
 * @author plesio
 */
public class TexturedMesh {

	private final Mesh mesh;
	private final Texture texture;

	public TexturedMesh(Mesh mesh, Texture modelTexture) {
		this.texture = modelTexture;
		this.mesh = mesh;
	}

	public Texture getModelTexture() {
		return this.texture;
	}

	public Mesh getMesh() {
		return this.mesh;
	}

}
