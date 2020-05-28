package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.textures.Texture;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class SimpleParticleSystem {

	private float pps;
	private float speed;
	private float gravityComplient;
	private float lifeLength;

	private Texture texture;

	public SimpleParticleSystem(float pps, float speed, float gravityComplient,
		float lifeLength, Texture texture) {
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.texture = texture;
	}

	public void generateParticles(Vector3f systemCenter) {
		float delta = DisplayManager.getDelta();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter);
		}
	}

	private void emitParticle(Vector3f center) {
		float dirX = (float) Math.random() * 2f - 1f;
		float dirZ = (float) Math.random() * 2f - 1f;
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalize();
		velocity.mul(speed);
		new Particle(new Vector3f(center), velocity, gravityComplient,
			lifeLength, 0, 1, texture);
	}
}
