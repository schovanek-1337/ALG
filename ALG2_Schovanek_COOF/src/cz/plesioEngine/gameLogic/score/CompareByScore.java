package cz.plesioEngine.gameLogic.score;

import java.util.Comparator;

/**
 *
 * @author plesio
 */
public class CompareByScore implements Comparator<ScoreboardItem> {

	@Override
	public int compare(ScoreboardItem o1, ScoreboardItem o2) {
		return o2.getScore() - o1.getScore();
	}
	
}
