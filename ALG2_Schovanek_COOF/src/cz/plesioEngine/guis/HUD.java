package cz.plesioEngine.guis;

import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Triplet;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class HUD {

	private static final float FONT_SIZE = 2f;
	private static final float MAX_LINE_LENGTH = 1f;
	private static final boolean CENTERED = false;

	private static FontType font;
	
	public static final DecimalFormat df = new DecimalFormat("#.##");


	private List<Triplet<Object, Object, Vector2f>> hudTexts = new ArrayList<>();

	public HUD() {
		Texture fontAtlas = TextureMaster.requestTexture("font/candara");
		font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/candara.fnt"));
	}
	
	public void addHuds(Triplet<Object, Object, Vector2f>... texts){
		hudTexts.addAll(Arrays.asList(texts));
	}

	/**
	 * Creates texts, renders them and deletes them from renderer.
	 */
	public void update() {
		for (Triplet<Object, Object, Vector2f> guis : hudTexts) {
			
			if (guis.getValue0() == null || guis.getValue1() == null) {
				continue;
			}
			
			String value1 = guis.getValue0().toString();
			
			if(guis.getValue0() instanceof Double){
				value1 = df.format(guis.getValue0());
			}
			
			String value2 = guis.getValue1().toString();
			
			if(guis.getValue1() instanceof Double){
				value2 = df.format(guis.getValue1());
			}
			
			GUIText newText = 
				new GUIText(value1 
					+ value2, 
					FONT_SIZE, font, guis.getValue2(), 
					MAX_LINE_LENGTH, CENTERED);
			
			newText.setColor(1, 1, 1);
			//154,156,231
			
			TextMaster.render();
			TextMaster.removeText(newText);
		}
		
		hudTexts.clear();

	}

}
