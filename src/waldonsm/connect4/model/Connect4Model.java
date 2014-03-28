package waldonsm.connect4.model;

import java.util.List;

public interface Connect4Model {

	/**
	 * Used to indicate Player 1
	 */
	public static final int PLAYER1_MARK = 1;
	/**
	 * Used to indicate Player 2
	 */
	public static final int PLAYER2_MARK = 2;
	/**
	 * Used to indicate that a cell is empty
	 */
	public static final int EMPTY_MARK = 0;
	
	/**
	 * Returns a COPY of the internal array.  The array should be accessed in the form
	 * <code>array[col][row]</code>.
	 * @return a copy of the internal array.
	 */
	public int[][] getArray();
	
	/**
	 * Returns the number of rows in the connect 4 board
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Returns the number of columns in the Connect 4 board
	 * @return the number of columns in the Connect 4 board
	 */
	public int getWidth();
	
	/**
	 * Gets the value at the row and column specified
	 * @param row the row
	 * @param col the column
	 * @return the value, which will be either PLAYER1_MARK, PLAYER2_MARK, or EMPTY_MARK
	 */
	public int getValueAt(int row, int col);
	
	/**
	 * Returns true if a piece can be added to the given column (i.e. if the column is not full)
	 * @param column the column in question
	 * @return true if a piece can be added to the column
	 */
	public boolean canAddPiece(int column);
	
	/**
	 * Returns a list of all the columns with empty top spaces which would be valid moves
	 * @return a list of all the columns with empty top spaces which would be valid moves
	 */
	public List<Integer> getAvailableMoves();
}
