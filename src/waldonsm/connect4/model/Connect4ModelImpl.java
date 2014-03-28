package waldonsm.connect4.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the basic implementation for a connect 4 board.
 * Very little error checking is provided.... so ArrayIndexOutOfBoundsExceptions are
 * your own fault.
 */
public final class Connect4ModelImpl implements Connect4Model {
	
	/**
	 * The board
	 */
	private final int[][] array;
	
	/**
	 * Creates a new Connect4Model with the given width and height
	 * @param height the height of the board (number of rows)
	 * @param width the width of the board (number of columns)
	 */
	public Connect4ModelImpl(int height, int width) {
		array = new int[width][height];
	}
	
	/**
	 * Adds a piece to the given column if it can
	 * @param column the column to add to
	 * @param playerNum the player who is adding to the column
	 * @return the row in that column that the piece wound up on
	 */
	public synchronized int addPiece(int column, int playerNum) {
		if (!canAddPiece(column))
			return -1;
		int row = array[column].length - 1;
		while (row > 0 && array[column][row-1] == EMPTY_MARK)
			row--;
		array[column][row] = playerNum;
		return row;
	}
	
	/**
	 * Clears the board in preparation for a new game.
	 * <P>
	 * Sets all tiles of the board to EMPTY_MARK
	 */
	public synchronized void clear() {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				array[i][j] = EMPTY_MARK;
			}
		}
	}
	
	/**
	 * Returns a COPY of the internal array.  The array should be accessed in the form
	 * <code>array[col][row]</code>.
	 * @return a copy of the internal array.
	 */
	public synchronized int[][] getArray() {
		int[][] arr = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				arr[i][j] = array[i][j];
			}
		}
		return arr;
	}
	
	/**
	 * Returns the number of rows in the connect 4 board
	 * @return
	 */
	public synchronized int getHeight() {
		return array[0].length;
	}
	
	/**
	 * Returns the number of columns in the Connect 4 board
	 * @return the number of columns in the Connect 4 board
	 */
	public synchronized int getWidth() {
		return array.length;
	}
	
	/**
	 * Gets the value at the row and column specified
	 * @param row the row
	 * @param col the column
	 * @return the value, which will be either PLAYER1_MARK, PLAYER2_MARK, or EMPTY_MARK
	 */
	public synchronized int getValueAt(int row, int col) {
		return array[col][row];
	}
	
	/**
	 * Returns a list of all the columns with empty top spaces which would be valid moves
	 */
	public synchronized List<Integer> getAvailableMoves() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int top = getHeight() -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i][top] == EMPTY_MARK) {
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * Returns true if a piece can be added to the given column (i.e. if the column is not full)
	 * @param column the column in question
	 * @return true if a piece can be added to the column
	 */
	public synchronized boolean canAddPiece(int column) {
		return array[column][array[0].length-1] == EMPTY_MARK;
	}
	
	/**
	 * Returns true if the given row and column is part of a winning run of 4
	 * @param col the column
	 * @param row the row
	 * @return true if the given row and column is part of a winning run of 4
	 */
	public synchronized boolean isWin(final int col, final int row) {
		if (array[col][row] == EMPTY_MARK)
			throw new IllegalArgumentException("No player has moved in this space");
		int count = 1;
		// test off diagonal (like forward slash /)
		int c = col;
		int r = row;
		while (c < array.length-2 && r < array[c].length-2 && array[c][r] == array[c+1][r+1]) {
			count++;
			c++;
			r++;
		}
		c = col;
		r = row;
		while (c > 0 && r > 0 && array[c][r] == array[c-1][r-1]) {
			count++;
			c--;
			r--;
		}
		if (count >= 4) {
			return true;
		}
		// test main diagonal (like backslash \)
		count = 1;
		c = col;
		r = row;
		while (c < array.length-2 && r > 0 && array[c][r] == array[c+1][r-1]) {
			count++;
			c++;
			r--;
		}
		c = col;
		r = row;
		while (c > 0 && r < array[c].length-2 && array[c][r] == array[c-1][r+1]) {
			count++;
			c--;
			r++;
		}
		if (count >= 4) {
			return true;
		}
		// test for side to side win
		count = 1;
		c = col;
		r = row;
		while (c < array.length-2 && array[c][r] == array[c+1][r]) {
			count++;
			c++;
		}
		c = col;
		while (c > 0 && array[c][r] == array[c-1][r]) {
			count++;
			c--;
		}
		if (count >= 4) {
			return true;
		}
		// test for vertical win
		count = 1;
		c = col;
		r = row;
		while (r < array[c].length-2 && array[c][r] == array[c][r+1]) {
			count++;
			r++;
		}
		r = row;
		while (r > 0 && array[c][r] == array[c][r-1]) {
			count++;
			r--;
		}
		if (count >= 4) {
			return true;
		}
		return false;
	}
}
