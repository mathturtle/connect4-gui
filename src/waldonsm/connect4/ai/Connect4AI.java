package waldonsm.connect4.ai;

import waldonsm.connect4.model.Connect4Model;

public interface Connect4AI {

	/**
	 * Tells the AI that it is now supposed to choose a move.
	 * <P>
	 * This method must return the move chosen
	 * @param model the Connect4Model with the current board
	 * @return the move chosen by the AI
	 */
	int chooseColumn(Connect4Model model);
	
	/**
	 * Tells the AI that a new game has been started, so it should flush any stored data.
	 */
	void newGame();

}
