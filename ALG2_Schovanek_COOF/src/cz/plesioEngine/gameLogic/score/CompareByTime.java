package cz.plesioEngine.gameLogic.score;

import java.util.Comparator;

/**
 *
 * @author plesio
 */
public class CompareByTime implements Comparator<ScoreboardItem> {

	@Override
	public int compare(ScoreboardItem o1, ScoreboardItem o2) {
		return (int) (o1.getTime() - o2.getTime());
	}
	
}
