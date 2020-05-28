package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.audio.Source;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.gameLogic.SettingMaster;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.toolbox.Maths;
import java.util.Random;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class GibletEmmiter {

	private String[] files;
	private int[] soundEffects;

	private float gibletAmount = 10;

	private Random random = new Random();
	private Source audioSource = new Source();

	public GibletEmmiter(String... files) {
		this.files = files;
		loadFiles();
		audioSource.setVolume(100);
	}

	private void loadFiles() {
		int i = 0;
		soundEffects = new int[files.length];
		for (String s : files) {
			soundEffects[i++] = AudioMaster.loadSound(s);
		}
	}

	private void playRandomSound(Vector3f position) {
		Random r = new Random();
		audioSource.setPosition(position);
		audioSource.play(soundEffects[r.nextInt(soundEffects.length)]);
	}

	public void emitGiblets(Vector3f center) {
		if(!SettingMaster.enableGore) return;
		center.y += 10;
		generateParticles(center, new Vector3f(0, 1, 0), 2f);
		playRandomSound(center);
	}

	public void generateParticles(Vector3f systemCenter, Vector3f direction,
		float spread) {
		for (int i = 0; i < gibletAmount; i++) {
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
		float randSize = 1 + random.nextFloat() * 2;

		Giblet giblet = new Giblet(center, randRotX, randRotY, randRotZ, randSize);
		giblet.accelerate(velocity, 1, 120);

		EntityRenderer.processEntity(giblet);
		PhysicsEngine.processPhysicsEntity(giblet);

	}

}
