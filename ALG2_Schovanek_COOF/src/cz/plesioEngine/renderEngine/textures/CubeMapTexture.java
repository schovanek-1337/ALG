package cz.plesioEngine.renderEngine.textures;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 *
 * @author plesio
 */
public class CubeMapTexture {

	private final int textureID;

	private TextureData[] textureData;

	protected CubeMapTexture(String[] textureFiles) {
		int newTexID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, newTexID);
		for (int i = 0; i < textureFiles.length; i++) {
			textureData = new TextureData[textureFiles.length];
			TextureData data = decodeTextureFile("res/" + textureFiles[i]
				+ ".png");
			this.textureData[i] = data;
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA,
				data.getWidth(), data.getHeight(), 0, GL_RGBA,
				GL_UNSIGNED_BYTE, data.getDecodedImage());
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S,
			GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T,
			GL_CLAMP_TO_EDGE);
		this.textureID = newTexID;
	}

	private static TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
			in.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.err.println("Tried to load texture " + fileName
				+ ", didn't work");
			System.exit(-1);
		}
		return new TextureData(width, height, buffer);
	}

	public int getTextureID() {
		return textureID;
	}

	public TextureData[] getTextureData() {
		return textureData;
	}

}
