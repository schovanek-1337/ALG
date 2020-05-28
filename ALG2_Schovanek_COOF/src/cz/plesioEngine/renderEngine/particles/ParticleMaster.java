package cz.plesioEngine.renderEngine.particles;

import cz.plesioEngine.renderEngine.textures.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * MASTER, MASTER.
 *
 * @author plesio
 */
public class ParticleMaster {

	private static Map<Texture, List<Particle>> particles
		= new HashMap<Texture, List<Particle>>();

	public static void init(ParticleShader shader) {
		ParticleRenderer.init(shader);
	}

	public static void update() {
		Iterator<Entry<Texture, List<Particle>>> mapIterator
			= particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext()) {
				Particle p = iterator.next();
				p.update();
				if (p.getIsDead()) {
					iterator.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			InsertionSort.sortHighToLow(list);
		}
	}

	public static void renderParticles() {
		ParticleRenderer.render(particles);
	}

	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getParticleTexture());
		if (list == null) {
			list = new ArrayList<>();
			particles.put(particle.getParticleTexture(), list);
		}
		list.add(particle);
	}

}
