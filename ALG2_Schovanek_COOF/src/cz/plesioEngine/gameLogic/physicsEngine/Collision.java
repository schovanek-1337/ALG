package cz.plesioEngine.gameLogic.physicsEngine;

import cz.plesioEngine.ECS.components.CAABB;
import cz.plesioEngine.entities.Camera;
import static java.lang.Math.max;
import static java.lang.Math.min;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Collision {

	public static boolean boundingBoxIntersection(AABB a, AABB b) {
		return (a.getMin()[0].getValue() <= b.getMax()[0].getValue() && a.getMax()[0].getValue() >= b.getMin()[0].getValue())
			&& (a.getMin()[1].getValue() <= b.getMax()[1].getValue() && a.getMax()[1].getValue() >= b.getMin()[1].getValue())
			&& (a.getMin()[2].getValue() <= b.getMax()[2].getValue() && a.getMax()[2].getValue() >= b.getMin()[2].getValue());
	}

	public static boolean boundingBoxIntersection(CAABB a, CAABB b) {
		return (a.min[0].value <= b.max[0].value && a.max[0].value >= b.min[0].value)
			&& (a.min[1].value <= b.max[1].value && a.max[1].value >= b.min[1].value)
			&& (a.min[2].value <= b.max[2].value && a.max[2].value >= b.min[2].value);
	}

	public static boolean boundingBoxRayIntersection(AABB box, Vector3f ray) {

		Vector3f dirFrac = new Vector3f();
		dirFrac.x = 1.0f / ray.x;
		dirFrac.y = 1.0f / ray.y;
		dirFrac.z = 1.0f / ray.z;

		float t1 = (box.getMin()[0].getValue() - Camera.getPositionInstance().x()) * dirFrac.x;
		float t2 = (box.getMax()[0].getValue() - Camera.getPositionInstance().x()) * dirFrac.x;
		float t3 = (box.getMin()[1].getValue() - Camera.getPositionInstance().y()) * dirFrac.y;
		float t4 = (box.getMax()[1].getValue() - Camera.getPositionInstance().y()) * dirFrac.y;
		float t5 = (box.getMin()[2].getValue() - Camera.getPositionInstance().z()) * dirFrac.z;
		float t6 = (box.getMax()[2].getValue() - Camera.getPositionInstance().z()) * dirFrac.z;

		float tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
		float tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

		if (tmax < 0) {
			return false;
		}

		return tmin <= tmax;
	}

	public static float boundingBoxRayIntersection(AABB box,
		Vector3f rayOrigin, Vector3f rayDirection) {

		float t[] = new float[10];
		t[1] = (box.getMin()[0].getValue() - rayOrigin.x) / rayDirection.x;
		t[2] = (box.getMax()[0].getValue() - rayOrigin.x) / rayDirection.x;
		t[3] = (box.getMin()[1].getValue() - rayOrigin.y) / rayDirection.y;
		t[4] = (box.getMax()[1].getValue() - rayOrigin.y) / rayDirection.y;
		t[5] = (box.getMin()[2].getValue() - rayOrigin.z) / rayDirection.z;
		t[6] = (box.getMax()[2].getValue() - rayOrigin.z) / rayDirection.z;
		t[7] = max(max(min(t[1], t[2]), min(t[3], t[4])), min(t[5], t[6]));
		t[8] = min(min(max(t[1], t[2]), max(t[3], t[4])), max(t[5], t[6]));
		t[9] = (t[8] < 0 || t[7] > t[8]) ? -1 : t[7];
		return t[9];

	}

	/**
	 * Returns the normal of a facing b
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vector3f normalOfCollidedFace(AABB a, AABB b) {

		//float maxXcandidate = a.getMin()[0].getValue() - b.getMax()[0]; // +X direction (1,0,0)
		//float minXcandidate = a.getMax()[0] - b.getMin()[0].getValue(); // -X direction (-1,0,0)
		//float maxYcandidate = a.getMin()[1].getValue() - b.getMax()[1].getValue(); // +Y direction (0,1,0)
		//float minYcandidate = a.getMax()[1].getValue() - b.getMin()[1].getValue(); // -Y direction (0,-1,0)
		//float maxZcandidate = a.getMin()[2].getValue() - b.getMax()[2].getValue(); // +Z direction (0,0,1)
		//float minZcandidate = a.getMax()[2].getValue() - b.getMin()[2].getValue(); // -Z direction (0,0,-1)
		int winnerPosition = 0;
		float winnerValue = Float.MAX_VALUE;

		float[] values = new float[6];
		values[0] = a.getMin()[0].getValue() - b.getMax()[0].getValue();
		values[1] = a.getMax()[0].getValue() - b.getMin()[0].getValue();

		values[2] = a.getMin()[1].getValue() - b.getMax()[1].getValue();
		values[3] = a.getMax()[1].getValue() - b.getMin()[1].getValue();

		values[4] = a.getMin()[2].getValue() - b.getMax()[2].getValue();
		values[5] = a.getMax()[2].getValue() - b.getMin()[2].getValue();

		for (int i = 0; i < 6; i++) {
			if (Math.abs(values[i]) < winnerValue) {
				winnerValue = Math.abs(values[i]);
				winnerPosition = i;
			}
		}

		switch (winnerPosition) {
			case 0:
				return new Vector3f(1, 0, 0);
			case 1:
				return new Vector3f(-1, 0, 0);
			case 2:
				return new Vector3f(0, 1, 0);
			case 3:
				return new Vector3f(0, -1, 0);
			case 4:
				return new Vector3f(0, 0, 1);
			case 5:
				return new Vector3f(0, 0, -1);
			default:
				return new Vector3f();
		}
	}

	public static Vector3f normalOfCollidedFace(CAABB a, CAABB b) {

		//float maxXcandidate = a.getMin()[0].getValue() - b.getMax()[0]; // +X direction (1,0,0)
		//float minXcandidate = a.getMax()[0] - b.getMin()[0].getValue(); // -X direction (-1,0,0)
		//float maxYcandidate = a.getMin()[1].getValue() - b.getMax()[1].getValue(); // +Y direction (0,1,0)
		//float minYcandidate = a.getMax()[1].getValue() - b.getMin()[1].getValue(); // -Y direction (0,-1,0)
		//float maxZcandidate = a.getMin()[2].getValue() - b.getMax()[2].getValue(); // +Z direction (0,0,1)
		//float minZcandidate = a.getMax()[2].getValue() - b.getMin()[2].getValue(); // -Z direction (0,0,-1)
		int winnerPosition = 0;
		float winnerValue = Float.MAX_VALUE;

		float[] values = new float[6];
		values[0] = a.min[0].value - b.max[0].value;
		values[1] = a.max[0].value - b.min[0].value;

		values[2] = a.min[1].value - b.max[1].value;
		values[3] = a.max[1].value - b.min[1].value;

		values[4] = a.min[2].value - b.max[2].value;
		values[5] = a.max[2].value - b.min[2].value;

		for (int i = 0; i < 6; i++) {
			if (Math.abs(values[i]) < winnerValue) {
				winnerValue = Math.abs(values[i]);
				winnerPosition = i;
			}
		}

		switch (winnerPosition) {
			case 0:
				return new Vector3f(1, 0, 0);
			case 1:
				return new Vector3f(-1, 0, 0);
			case 2:
				return new Vector3f(0, 1, 0);
			case 3:
				return new Vector3f(0, -1, 0);
			case 4:
				return new Vector3f(0, 0, 1);
			case 5:
				return new Vector3f(0, 0, -1);
			default:
				return new Vector3f();
		}
	}

}
