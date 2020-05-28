package cz.plesioEngine.toolbox;

import org.joml.Vector3f;

/**
 * Fuzzy maths, for all your fuzzy needs.
 * @author plesio
 */
public class FuzzyMaths {

	public static boolean compareVectorsFuzzy(Vector3f value1, Vector3f value2,
		float fuzzyFactor) {

		float x = Math.abs(value1.x() - value2.x());
		float y = Math.abs(value1.y() - value2.y());
		float z = Math.abs(value1.z() - value2.z());

		return (x < fuzzyFactor && y < fuzzyFactor && z < fuzzyFactor);
	}

	public static boolean compareFloatsFuzzy(float value1, float value2,
		float fuzzyFactor) {
		return Math.abs(value1 - value2) < fuzzyFactor;
	}

}
