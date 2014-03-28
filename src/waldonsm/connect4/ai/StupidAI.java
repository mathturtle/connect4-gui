package waldonsm.connect4.ai;

import java.util.List;
import java.util.Random;

import waldonsm.connect4.model.Connect4Model;

public class StupidAI implements Connect4AI {

	@Override
	public int chooseColumn(Connect4Model model) {
		Random rand = new Random();
		List<Integer> list = model.getAvailableMoves();
		return list.get(rand.nextInt(list.size()));
	}

	@Override
	public void newGame() {
		// does nothing
	}

}
