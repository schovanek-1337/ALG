package cz.plesioEngine.gameLogic.score;

import cz.plesioEngine.guis.fontMeshCreator.FontType;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import org.joml.Vector2f;

/**
 *
 * @author plesio
 */
public class Scoreboard {

	private static FontType font;

	private static List<ScoreboardItem> scoreboardItems;

	/**
	 *
	 */
	public static void init() {
		Texture fontAtlas = TextureMaster.requestTexture("font/arial");
		font = new FontType(fontAtlas.getTextureID(),
			new File("res/font/arial.fnt"));
		ScoreLoader.loadScore();
		scoreboardItems = ScoreLoader.getScoreboard();
	}
	
	/**
	 *
	 */
	public static void reset(){
		scoreboardItems.clear();
		ScoreLoader.loadScore();
		scoreboardItems = ScoreLoader.getScoreboard();
	}
	
	/**
	 *
	 */
	public static void delete(){
		scoreboardItems.clear();
	}

	/**
	 *
	 * @param sort
	 * @param position
	 */
	public static void printScoreboard(Comparator<ScoreboardItem> sort,
		Vector2f position) {

		Vector2f nextPosition = new Vector2f(position);

		scoreboardItems.sort(sort);

		GUIText topText = new GUIText(
			" ::SCOREBOARD:: ",
			2, font, nextPosition, 1, false);
		topText.setColor(1, 1, 1);

		TextMaster.render();
		TextMaster.removeText(topText);

		nextPosition.y += 0.05f;
		
		int counter = 0;
		
		for (ScoreboardItem item : scoreboardItems) {
			
			if (counter > 10){
				continue;
			}

			String val1 = String.valueOf(item.getScore());
			String scoreText = generate(() -> " ")
				.limit((11 - val1.length()) / 2).collect(joining());

			scoreText = scoreText + val1;

			scoreText = scoreText + generate(() -> " ")
				.limit((11 - val1.length()) / 2).collect(joining());

			String val2 = String.valueOf(item.getTime());

			String timeText = generate(() -> " ")
				.limit((10 - val2.length()) / 2).collect(joining());

			timeText = timeText + val2;

			timeText = timeText + generate(() -> " ")
				.limit((10 - val2.length()) / 2).collect(joining());
			
			String val3 = item.getName();
			
			String nameText = generate(() -> " ")
				.limit((10 - val3.length()) / 2).collect(joining());

			nameText = nameText + val3;

			nameText = nameText + generate(() -> " ")
				.limit((10 - val3.length()) / 2).collect(joining());

			GUIText itemText = new GUIText(
				scoreText + " " + timeText + " "
				+ nameText + " " + item.getTimestamp(),
				2, font, nextPosition, 1, false);
			itemText.setColor(1, 1, 1);
			nextPosition.y += 0.05f;

			TextMaster.render();
			TextMaster.removeText(itemText);
			
			counter++;

		}

	}

}
