package cz.plesioEngine.entities.staticEntities;

import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.util.HashMap;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public final class EntityCreator {

	private static HashMap<String, TexturedMesh> existingEntities
		= new HashMap<>();

	private EntityCreator() {
	}

	public static Entity createEntity(String fileName, String textureName) {

		if (existingEntities.containsKey(fileName)) {
			return new Entity(existingEntities.get(fileName),
				new Vector3f(0, 0, 0), 0, 0, 0, 1);
		}

		Mesh entityMesh = MeshMaster.requestMesh(fileName);
		Texture entityTexture = TextureMaster.requestTexture(textureName);
		TexturedMesh texturedMesh = new TexturedMesh(entityMesh, entityTexture);

		Entity newEntity = new Entity(texturedMesh,
			new Vector3f(0, 0, 0), 0, 0, 0, 1);

		existingEntities.put(fileName, texturedMesh);

		return newEntity;
	}

	public static Entity createEntity(String fileName, String textureName,
		int textureAtlasIndex) {

		Mesh entityMesh = MeshMaster.requestMesh(fileName);
		Texture entityTexture = TextureMaster.requestTexture(textureName);
		TexturedMesh texturedMesh = new TexturedMesh(entityMesh, entityTexture);

		Entity newEntity = new Entity(texturedMesh,
			new Vector3f(0, 0, 0), 0, 0, 0, 1, textureAtlasIndex);

		return newEntity;
	}

	public static Entity createEntity(TexturedMesh model) {

		Entity newEntity = new Entity(model, new Vector3f(0, 0, 0), 0, 0, 0, 1);

		return newEntity;
	}

	public static Entity createEntity(TexturedMesh model, int textureAtlasIndex) {

		Entity newEntity
			= new Entity(model, new Vector3f(0, 0, 0), 0, 0, 0, 1,
				textureAtlasIndex);

		return newEntity;
	}

}
