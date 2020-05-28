package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.textures.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;

	private Texture texture;

	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;

	private boolean dead = false;
	private float elapsedTime = 0;

	private float distance;

	public Particle(Vector3f position, Vector3f velocity, float gravityEffect,
		float lifeLength, float rotation, float scale, Texture texture) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = -gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	public Texture getParticleTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public boolean getIsDead() {
		return dead;
	}

	public float getDistance() {
		return distance;
	}

	/**
	 * Called on every frame, moves the particle and decides if it should
	 * die. Go forth and die.
	 */
	protected void update() {
		updateTextureCoordInfo();
		velocity.y += PhysicsEngine.GRAVITY * gravityEffect
			* DisplayManager.getDelta();
		Vector3f change = new Vector3f(velocity);
		change.mul(DisplayManager.getDelta());
		position.add(change);
		distance = Camera.getPositionInstance().sub(position).lengthSquared();
		elapsedTime += DisplayManager.getDelta();
		dead = elapsedTime > lifeLength;
	}

	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}

}
