package cz.plesioEngine.toolbox;

import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.MasterRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Represents a ray coming from the mouse cursor into the world space.
 *
 * @author plesio
 */
public class MousePicker {

	//private static final int RECURSION_COUNT = 200;
	//private static final float RAY_RANGE = 600;

	private static Vector3f currentRay;

	private static Matrix4f viewMatrix;
	private static Matrix4f projectionMatrix;

	public static void init() {
		viewMatrix = Maths.createViewMatrix();
		projectionMatrix = MasterRenderer.getProjectionMatrix();
		update();
	}

	public static Vector3f getCurrentRay() {
		if (currentRay == null) {
			return null;
		}
		return new Vector3f(currentRay.x, currentRay.y, currentRay.z);
	}

	public static void update() {
		viewMatrix = Maths.createViewMatrix();
		currentRay = calculateMouseRay();
	}

	private static Vector3f calculateMouseRay() {
		Vector2f mousePos = DisplayManager.getCurrentMousePos();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mousePos);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = projectionMatrix.invert().transform(clipCoords);
		eyeCoords = new Vector4f(eyeCoords.x, eyeCoords.y, -1.0f, 0.0f);
		Vector4f mouseRay = viewMatrix.invert().transform(eyeCoords).normalize();
		return new Vector3f(mouseRay.x, mouseRay.y, mouseRay.z);
	}

	private static Vector2f getNormalizedDeviceCoords(Vector2f mousePosition) {
		float x = (2.0f * mousePosition.x())
			/ DisplayManager.getWindowDimensions().y() - 1.0f;

		float y = 1.0f - (2.0f * mousePosition.y())
			/ DisplayManager.getWindowDimensions().x();

		return new Vector2f(x, y);
	}

}
