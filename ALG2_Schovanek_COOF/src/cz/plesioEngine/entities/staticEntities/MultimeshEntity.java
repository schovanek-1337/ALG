package cz.plesioEngine.entities.staticEntities;

import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.renderEngine.textures.Texture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class MultimeshEntity {

	private Entity[] entities;

	private Vector3f position;

	private float rotX, rotY, rotZ, scale;

	/**
	 *
	 * @param objFileNames
	 * @param textureFileNames
	 * @param position
	 * @param rotX
	 * @param rotY
	 * @param rotZ
	 * @param scale
	 */
	public MultimeshEntity(String[] objFileNames,
		String[] textureFileNames, Vector3f position, float rotX, float rotY,
		float rotZ, float scale) {

		entities = new Entity[objFileNames.length];

		for (int i = 0; i < objFileNames.length; i++) {
			entities[i] = EntityCreator.createEntity(objFileNames[i],
				textureFileNames[i]);
		}

		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;

		updateTransformations();
	}

	/**
	 *
	 * @param objFileName
	 * @param textures
	 * @param position
	 * @param rotX
	 * @param rotY
	 * @param rotZ
	 * @param scale
	 */
	public MultimeshEntity(String objFileName,
		Texture[] textures, Vector3f position, float rotX, float rotY,
		float rotZ, float scale) {

		entities = new Entity[textures.length];

		Mesh[] meshes = MeshMaster.requestMeshGroup(objFileName);

		for (int i = 0; i < textures.length; i++) {
			entities[i] = new Entity(new TexturedMesh(meshes[i], textures[i]),
				position, rotX, rotY, rotZ, scale);
		}

		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;

		updateTransformations();
	}

	public MultimeshEntity(String objFileName, Texture texture,
		Vector3f position, float rotX, float rotY, float rotZ,
		float scale) {

		Mesh[] meshes = MeshMaster.requestMeshGroup(objFileName);

		entities = new Entity[meshes.length];

		for (int i = 0; i < meshes.length; i++) {
			entities[i] = new Entity(new TexturedMesh(meshes[i], texture),
				position, rotX, rotY, rotZ, scale);
		}

		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;

		updateTransformations();
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		updateTransformations();
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
		updateTransformations();
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
		updateTransformations();
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
		updateTransformations();
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		updateTransformations();
	}

	public Entity[] getEntitiesArray() {
		return entities;
	}

	public List<Entity> getEntitiesList() {
		List<Entity> ents = new ArrayList<>();
		ents.addAll(Arrays.asList(entities));
		return ents;
	}

	private void updateTransformations() {
		for (Entity e : entities) {
			e.setPosition(position);
			e.setRotX(rotX);
			e.setRotY(rotY);
			e.setRotZ(rotZ);
			e.setScale(scale);
		}
	}

}
