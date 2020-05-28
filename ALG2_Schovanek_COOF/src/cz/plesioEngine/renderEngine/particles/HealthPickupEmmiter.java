package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.toolbox.Maths;
import java.util.Random;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class HealthPickupEmmiter {

	private int healthAmountMax = 2;

	private Random random = new Random();

	public void emitHealthPickups(Vector3f center) {
		center.y += 10;
		generateParticles(center, new Vector3f(0, 1, 0), 2f);
	}

	private void generateParticles(Vector3f systemCenter, Vector3f direction,
		float spread) {
		for (int i = 0; i < random.nextInt(healthAmountMax + 1); i++) {
			emitParticle(systemCenter, direction, spread);
		}
	}

	private void emitParticle(Vector3f center, Vector3f direction, float spread) {
		Vector3f velocity = new Vector3f(direction.x(), direction.y(), direction.z());

		velocity.add(Maths.randomPointInsideUnitSphere().mul(spread));

		velocity.normalize();

		float randRotX = random.nextFloat() * 360;
		float randRotY = random.nextFloat() * 360;
		float randRotZ = random.nextFloat() * 360;

		HealthPickup h = new HealthPickup(center, randRotX, randRotY, randRotZ, 3);
		h.accelerate(velocity, 5, 25);

		EntityRenderer.processEntity(h);
		PhysicsEngine.processPhysicsEntity(h);

	}
}
