package cz.plesioEngine.renderEngine.textures;

import java.nio.ByteBuffer;

/**
 *
 * @author plesio
 */
public class TextureData {

	private int width;
	private int height;
	private ByteBuffer decodedImage;

	public TextureData(int width, int height, ByteBuffer decodedImage) {
		this.width = width;
		this.height = height;
		this.decodedImage = decodedImage;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getDecodedImage() {
		return decodedImage;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setDecodedImage(ByteBuffer decodedImage) {
		this.decodedImage = decodedImage;
	}

}
