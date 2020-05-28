package cz.plesioEngine.renderEngine.textures;

import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.Loader;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL30;

/**
 * Represents a texture in OpenGL's memory
 *
 * @author plesio
 */
public class Texture {

	private int textureID;

	private final TextureData textureData;
	private BufferedImage imageFile;

	private int numberOfRows = 1;

	private float shineDamper = 1;
	private float reflectivity = 0.15f;

	private float tileSize = 1.0f;

	private boolean isTransparent = false;
	private boolean hasFakeLighting = false;

	/**
	 * Creates a texture object and loads a PNG into it.
	 *
	 * @param fileName The name of the texture located in ./res/
	 */
	protected Texture(String fileName) {
		textureData = new TextureData(0, 0, null);
		loadTexture(fileName);
	}

	/**
	 *
	 * @return TextureID in OpenGL's memory
	 */
	public int getTextureID() {
		return textureID;
	}

	private void loadTexture(String fileName) {
		try {
			InputStream is = new FileInputStream(fileName);

			BufferedImage image = ImageIO.read(is);
			this.imageFile = image;

			this.textureData.setWidth(image.getWidth());
			this.textureData.setHeight(image.getHeight());

			// Load texture contents into a byte buffer
			ByteBuffer buf = decodePNG(image);
			this.textureData.setDecodedImage(buf);

			// Create a new OpenGL texture
			this.textureID = glGenTextures();
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, this.textureID);

			// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			// Upload the texture data
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureData.getWidth(),
				textureData.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

			GL30.glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
				GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
				-0.4f);
			if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				float amount = Math.min(DisplayManager.anisotropyLevel,
					glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				glTexParameterf(GL11.GL_TEXTURE,
					EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
			}
		} catch (IOException ex) {
			Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected ByteBuffer decodePNG(BufferedImage image) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// Load texture contents into a byte buffer
		ByteBuffer buf = ByteBuffer.allocateDirect(
			4 * imageWidth * imageHeight);

		// decode image
		// ARGB format to -> RGBA
		for (int h = 0; h < imageHeight; h++) {
			for (int w = 0; w < imageWidth; w++) {
				int argb = image.getRGB(w, h);
				buf.put((byte) (0xFF & (argb >> 16)));
				buf.put((byte) (0xFF & (argb >> 8)));
				buf.put((byte) (0xFF & (argb)));
				buf.put((byte) (0xFF & (argb >> 24)));
			}
		}
		buf.flip();
		return buf;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public void setTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
	}

	public boolean hasFakeLighting() {
		return hasFakeLighting;
	}

	public void setHasFakeLighting(boolean hasFakeLighting) {
		this.hasFakeLighting = hasFakeLighting;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public TextureData getTextureData() {
		return textureData;
	}

	public BufferedImage getImageFile() {
		return imageFile;
	}

	public float getTileSize() {
		return tileSize;
	}

	public void setTileSize(float tileSize) {
		this.tileSize = tileSize;
	}

}
