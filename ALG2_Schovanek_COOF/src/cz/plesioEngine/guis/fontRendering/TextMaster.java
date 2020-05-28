package cz.plesioEngine.guis.fontRendering;

import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.guis.fontMeshCreator.TextMeshData;
import cz.plesioEngine.renderEngine.Loader;
import cz.plesioEngine.toolbox.Converter;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author plesio
 */
public class TextMaster {

	private static final Map<FontType, List<GUIText>> texts
		= new HashMap<FontType, List<GUIText>>();

	public static void render() {
		FontRenderer.render(texts);
	}

	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		
		List<GUIText> list = texts.get(font);
		if(list != null && list.contains(text)){
			return;
		}

		FloatBuffer vertexPositions
			= Converter.arrayToBuffer(data.getVertexPositions());
		FloatBuffer textureCoords
			= Converter.arrayToBuffer(data.getTextureCoords());

		int vao = Loader.loadTextToVao(vertexPositions, textureCoords);

		text.setMeshInfo(vao, data.getVertexCount());

		List<GUIText> textBatch = texts.get(font);

		if (textBatch == null) {
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}

		textBatch.add(text);
	}
	
	public static void clear(){
		texts.clear();
	}

	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());

		if (textBatch == null) {
			return;
		}

		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

}
