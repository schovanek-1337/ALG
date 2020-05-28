package cz.plesioEngine.toolbox;

import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.renderEngine.DisplayManager;
import static cz.plesioEngine.renderEngine.MasterRenderer.*;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author plesio
 */
public final class Maths {

	/**
	 * Barycentric magic.
	 * @param p1 uhh
	 * @param p2 yeah
	 * @param p3 i copied it
	 * @param pos from somewhere
	 * @return and i don't know what it does.
	 */
	public static float baryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	/**
	 * Builds a transformation matrix meant for 3D objects.
	 * @param translation object translation.
	 * @param rx object rotation on x axis.
	 * @param ry object rotation on y axis.
	 * @param rz object rotation on z axis.
	 * @param scale object scale.
	 * @return 
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation,
		float rx, float ry, float rz, float scale) {
		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.translate(translation);
		transformationMatrix.rotate((float) StrictMath.toRadians(rx),
			new Vector3f(1, 0, 0));
		transformationMatrix.rotate((float) StrictMath.toRadians(ry),
			new Vector3f(0, 1, 0));
		transformationMatrix.rotate((float) StrictMath.toRadians(rz),
			new Vector3f(0, 0, 1));
		transformationMatrix.scale(scale);
		return transformationMatrix;
	}

	/**
	 * Builds a view matrix from the static camera.
	 * @return 
	 */
	public static Matrix4f createViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotate((float) Math.toRadians(Camera.getPitch()),
			new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(Camera.getYaw()),
			new Vector3f(0, 1, 0));
		viewMatrix.rotate((float) Math.toRadians(Camera.getRoll()),
			new Vector3f(0, 0, 1));
		Vector3f cameraPos = Camera.getPositionInstance();
		Vector3f negativeCameraPos = new Vector3f(
			-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;
	}

	/**
	 * Build a projection matrix based on the parameters from the Master Renderer.
	 * @return projection matrix.
	 */
	public static Matrix4f createProjectionMatrix() {
		IntBuffer h = BufferUtils.createIntBuffer(4);
		IntBuffer w = BufferUtils.createIntBuffer(4);

		GLFW.glfwGetWindowSize(DisplayManager.getWindowHandle(), w, h);

		int width = w.get(0);
		int height = h.get(0);

		float aspectRatio = width / (float) height;
		float y_scale = (float) ((1f
			/ StrictMath.tan(StrictMath.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix._m00(x_scale);
		projectionMatrix._m11(y_scale);
		projectionMatrix._m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix._m23(-1);
		projectionMatrix._m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix._m33(0);

		return projectionMatrix;
	}

	/**
	 * Creates a transformation matrix used for GUIs.
	 * @param translation GUI position (0 - 1)
	 * @param scale GUI Scale
	 * @return transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(translation.x, translation.y, 1));
		matrix.scale(new Vector3f(scale.x, scale.y, 1f));
		return matrix;
	}

	/**
	 * Returns a random point inside a unit sphere.
	 * @return random float vector.
	 */
	public static Vector3f randomPointInsideUnitSphere() {
		double u = Math.random();
		double v = Math.random();
		double theta = u * 2.0f * Math.PI;
		double phi = Math.acos(2.0f * v - 1.0f);
		double r = Math.cbrt(Math.random());
		double sinTheta = Math.sin(theta);
		double cosTheta = Math.cos(theta);
		double sinPhi = Math.sin(phi);
		double cosPhi = Math.cos(phi);
		double x = r * sinPhi * cosTheta;
		double y = r * sinPhi * sinTheta;
		double z = r * cosPhi;
		return new Vector3f((float) x, (float) y, (float) z);
	}

	/**
	 * Tests for a point existing in a circle.
	 *
	 * @param circle_x
	 * @param circle_y
	 * @param x
	 * @param y
	 * @param rad
	 * @return
	 */
	public static boolean isInside(float circle_x, float circle_y,
		float x, float y, float rad) {
		return (x - circle_x) * (x - circle_x)
			+ (y - circle_y) * (y - circle_y) <= rad * rad;
	}

	public static float[] simpleMatrixVectorMultiply(float x, float y, float z,
		Matrix4f m) {

		Vector4f holdingVector = new Vector4f(x, y, z, 1);

		holdingVector.mul(m);

		float[] returnArray = new float[3];

		returnArray[0] = holdingVector.x();
		returnArray[1] = holdingVector.y();
		returnArray[2] = holdingVector.z();

		return returnArray;
	}

}
