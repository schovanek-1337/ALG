package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.toolbox.Maths;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class SpreadParticleSystem {

	private float particleAmount;
	private float speed;
	private float gravityComplient;
	private float lifeLength;

	private Texture texture;

	public SpreadParticleSystem(float particleAmount, float speed, float gravityComplient,
		float lifeLength, Texture texture) {
		this.particleAmount = particleAmount;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.texture = texture;
	}

	public void generateParticles(Vector3f systemCenter, Vector3f direction,
		float spread) {
		for (int i = 0; i < particleAmount; i++) {
			emitParticle(systemCenter, direction, spread);
		}
	}

	private void emitParticle(Vector3f center, Vector3f direction, float spread) {
		Vector3f velocity = new Vector3f(direction.x(), direction.y(), direction.z());

		velocity.add(Maths.randomPointInsideUnitSphere().mul(spread));

		velocity.normalize();

		velocity.mul(speed);
		new Particle(new Vector3f(center), velocity, gravityComplient,
			lifeLength, 0, 1, texture);
	}

	public float getParticleAmount() {
		return particleAmount;
	}

	public void setParticleAmount(float particleAmount) {
		this.particleAmount = particleAmount;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravityComplient() {
		return gravityComplient;
	}

	public void setGravityComplient(float gravityComplient) {
		this.gravityComplient = gravityComplient;
	}

	public float getLifeLength() {
		return lifeLength;
	}

	public void setLifeLength(float lifeLength) {
		this.lifeLength = lifeLength;
	}

}
