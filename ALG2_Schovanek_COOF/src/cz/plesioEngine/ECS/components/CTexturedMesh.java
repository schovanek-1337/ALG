package cz.plesioEngine.ECS.components;

import cz.plesioEngine.ECS.EntityManager;
import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.textures.Texture;

/**
 * Represents a mesh paired with a texture.
 *
 * @author plesio
 */
public final class CTexturedMesh implements IComponent {

	public int entityID;
	public int componentListIndex = INVALID_COMPONENT_ID;
	public Mesh mesh;
	public Texture texture;
	public int textureAtlasIndex;

	public CTexturedMesh(Mesh m, Texture tex) {
		mesh = m;
		texture = tex;
		textureAtlasIndex = 0;
		entityID = -1;
		notifyCreation();
	}

	public CTexturedMesh(Mesh m, Texture tex, int texAtlasIndex) {
		mesh = m;
		texture = tex;
		textureAtlasIndex = texAtlasIndex;
		entityID = -1;
		notifyCreation();
	}

	@Override
	public void notifyCreation() {
		componentListIndex = EntityManager.createComponent(this);
	}

	@Override
	public void setEntityID(int ID) {
		this.entityID = ID;
	}

	@Override
	public int getEntityID() {
		return entityID;
	}

	@Override
	public void setComponentListIndex(int index) {
		componentListIndex = index;
	}

}
