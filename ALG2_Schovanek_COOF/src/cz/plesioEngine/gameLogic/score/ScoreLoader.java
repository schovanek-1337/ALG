package cz.plesioEngine.gameLogic.score;

import cz.plesioEngine.gameLogic.PlayerStats;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author plesio
 */
public class ScoreLoader {
	
	private static List<ScoreboardItem> scoreboard = new ArrayList<>();

	/**
	 * Appends a new score to the file
	 * @param playerName player's name.
	 */
	public static void saveScore(String playerName) {

		StringBuilder sb = new StringBuilder();
		
		String formattedTime = String.format("%.2f", PlayerStats.time);
		
		String formattedDate = LocalDateTime.now().toString();
		
		sb.append(playerName).append(" ").append(formattedTime)
			.append(" ").append(PlayerStats.score).append(" ")
			.append(formattedDate).append("\n");
		
		try {
			File f = new File("data/score.txt");
			
			if(!f.exists()){
				f.createNewFile();
			}
			
			try (FileWriter fw = new FileWriter("data/score.txt", true)) {
				fw.write(sb.toString());
			}

		} catch (IOException ex) {
			Logger.getLogger(ScoreLoader.class.getName()).
				log(Level.SEVERE, null, ex);
		}

	}
	
	/**
	 * Loads the scores from a file
	 */
	public static void loadScore() {
		
		scoreboard.clear();

		StringBuilder sb = new StringBuilder();
		
		try {
			File scoreFile = new File("data/score.txt");
			
			if(!scoreFile.exists()){
				return;
			}
			
			Files.lines(scoreFile.toPath()).forEach((t) -> {
				sb.append(t).append("\n");
				
				String[] elements = t.split(" ");

				scoreboard.add(new ScoreboardItem(
					Integer.parseInt(elements[2]), 
					Double.parseDouble(elements[1]), 
					elements[0], 
					elements[3]));
				
			});
			
		} catch (IOException ex) {
			Logger.getLogger(ScoreLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public static List<ScoreboardItem> getScoreboard(){
		return new ArrayList<>(scoreboard);
	}

}
