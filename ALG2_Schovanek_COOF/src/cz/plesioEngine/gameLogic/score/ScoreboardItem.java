package cz.plesioEngine.gameLogic.score;

/**
 *
 * @author plesio
 */
public class ScoreboardItem implements Comparable<ScoreboardItem>{

	private int score;
	private double time;
	private String name;
	private String timestamp;

	public ScoreboardItem(int score, double time,
		String name, String timestamp) {
		this.score = score;
		this.time = time;
		this.name = name;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ScoreboardItem{" + "score="
			+ score + ", time=" + time + ", "
			+ "name=" + name + ", "
			+ "timestamp=" + timestamp + '}';
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(ScoreboardItem o) {
		return this.getScore() - o.getScore();
	}

}
