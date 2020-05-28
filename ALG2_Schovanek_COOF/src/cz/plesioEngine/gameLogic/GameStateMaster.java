package cz.plesioEngine.gameLogic;

/**
 *
 * @author plesio
 */
public final class GameStateMaster {

	private static GameState currentState;
	private static GameState previousGameState;

	public static void init(GameState state) {
		currentState = state;
	}

	public static GameState getCurrentGameState() {
		return currentState;
	}

	public static GameState getPreviousGameState() {
		return previousGameState;
	}

	public static void setGameState(GameState state) {
		previousGameState = currentState;
		currentState = state;
	}

}
