package cz.plesioEngine.entities.staticEntities;

import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.gameLogic.physicsEngine.AABB;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Entity {

	private final AABB aabb;
	private TexturedMesh texturedMesh;
	private Vector3f position;
	protected float rotX, rotY, rotZ;
	protected float scale;
	private int textureIndex = 0;

	private boolean disableRender = false;

	public Entity(TexturedMesh texturedMesh, Vector3f position, float rotX,
		float rotY, float rotZ, float scale) {
		this.texturedMesh = texturedMesh;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.aabb = new AABB(this);
	}

	public Entity(TexturedMesh texturedMesh, Vector3f position, float rotX,
		float rotY, float rotZ, float scale, int textureIndex) {
		this.texturedMesh = texturedMesh;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.textureIndex = textureIndex;
		this.aabb = new AABB(this);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public float getTextureXOffset() {
		int column = textureIndex % texturedMesh.getModelTexture().getNumberOfRows();
		return column / (float) texturedMesh.getModelTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureIndex / texturedMesh.getModelTexture().getNumberOfRows();
		return row / (float) texturedMesh.getModelTexture().getNumberOfRows();
	}

	private void createBoundingBox() {
		this.aabb.generateBoundingBox(this);
	}

	public AABB getBoundingBox() {
		return aabb;
	}

	public TexturedMesh getTexturedModel() {
		return texturedMesh;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setTexturedMesh(TexturedMesh texturedModel) {
		this.texturedMesh = texturedModel;
	}

	public void setPosition(Vector3f position) {
		if (position != null) {
			this.position = position;
		}
	}

	public void setPosition(float x, float y, float z) {
		this.position = new Vector3f(x, y, z);
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
		createBoundingBox();
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
		createBoundingBox();
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
		createBoundingBox();
	}

	public void setScale(float scale) {
		this.scale = scale;
		createBoundingBox();
	}

	public int getVaoID() {
		return this.getTexturedModel().getMesh().vaoID;
	}

	public boolean isDisableRender() {
		return disableRender;
	}

	public void setDisableRender(boolean disableRender) {
		this.disableRender = disableRender;
	}

}
