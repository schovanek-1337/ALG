package cz.plesioEngine.guis;

import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.textures.Texture;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class GuiTexture {

	private final Texture texture;
	private final Vector2f position;
	private final Vector2f scale;
	
	private float transparency = 0.0f;

	public GuiTexture(Texture texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.scale.x = this.scale.x / DisplayManager.getAspectRatio();
	}

	public Texture getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	public float getTransparency(){
		return transparency;
	}
	
	public void setTransparency(float opacity){
		this.transparency = opacity;
	}

}
