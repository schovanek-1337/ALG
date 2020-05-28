package cz.plesioEngine.entities.lights;

import cz.plesioEngine.renderEngine.MasterRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public final class LightMaster {

	public static final int LIGHT_LIMIT = 16;

	private static final Map<String, Light> dynamicLights = new HashMap<>();
	private static final Map<String, Light> staticLights = new HashMap<>();

	private LightMaster() {
	}

	public static void createLight(String lightName, Vector3f location,
		Vector3f color, boolean staticLight) {

		if (dynamicLights.size() + 1 > LIGHT_LIMIT) {
			System.err.println("Light " + lightName + " over limit!");
		}
		if (staticLight) {
			if (!staticLights.containsKey(lightName)) {
				staticLights.put(lightName, new Light(location, color,
					staticLight));

				MasterRenderer.updateStaticLightSources();
			}
		} else {
			if (!dynamicLights.containsKey(lightName)) {
				dynamicLights.put(lightName, new Light(location, color,
					staticLight));
			}
		}
	}

	public static void createLight(String lightName, Vector3f location,
		Vector3f color, Vector3f attenuation, boolean staticLight) {

		if (dynamicLights.size() + 1 > LIGHT_LIMIT) {
			System.err.println("Light " + lightName + " over limit!");
		}
		if (staticLight) {
			if (!staticLights.containsKey(lightName)) {
				staticLights.put(lightName, new Light(location, color, attenuation,
					staticLight));

				MasterRenderer.updateStaticLightSources();
			}
		} else {
			if (!dynamicLights.containsKey(lightName)) {
				dynamicLights.put(lightName, new Light(location, color, attenuation,
					staticLight));
			}
		}
	}

	public static Light getLight(String name) {
		if (dynamicLights.containsKey(name)) {
			return dynamicLights.get(name);
		}
		if (staticLights.containsKey(name)) {
			return staticLights.get(name);
		}
		return null;
	}

	public static List<Light> getDynamicLights() {
		return new ArrayList<>(dynamicLights.values());
	}

	public static List<Light> getStaticLights() {
		return new ArrayList<>(staticLights.values());
	}

}
