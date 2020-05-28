package cz.plesioEngine.gameLogic;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.audio.Source;
import cz.plesioEngine.guis.AnimatedGUITexture;
import cz.plesioEngine.guis.GuiRenderer;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class Overkill {

	private static int overkillSFXBuffer
		= AudioMaster.loadSound("res/sound/overkill.ogg");
	
	private static Source overkillSource = new Source();

	private static AnimatedGUITexture texture
		= new AnimatedGUITexture(
			TextureMaster.requestTexture("gui/overkill"),
			new Vector2f(0, 0.4f),
			new Vector2f(1.8f, 1f));
	
	public static void init(){
		overkillSource.setVolume(1000);
	}

	private static void reset() {
		texture.setTransparency(0);
	}

	public static void overkill() {
		reset();
		GuiRenderer.processGUI(texture);
		overkillSource.play(overkillSFXBuffer);
		texture.fadeOut();
	}

}
