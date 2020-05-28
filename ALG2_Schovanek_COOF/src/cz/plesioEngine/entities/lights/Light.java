package cz.plesioEngine.entities.lights;

import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Light {

	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);

	/**
	 * If the light is static, it won't be updated in the shader every frame
	 */
	private boolean isStatic;

	protected Light(Vector3f position, Vector3f color, boolean staticLight) {
		this.position = position;
		this.color = color;
		this.isStatic = staticLight;
	}

	protected Light(Vector3f position, Vector3f color, Vector3f attenuation,
		boolean staticLight) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.isStatic = staticLight;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public void setIsStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isIsStatic() {
		return isStatic;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

}
