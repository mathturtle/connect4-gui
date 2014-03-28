package waldonsm.connect4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import waldonsm.connect4.ai.Connect4AI;
import waldonsm.connect4.gui.animation.Animation;
import waldonsm.connect4.gui.animation.AnimationDoneListener;
import waldonsm.connect4.gui.animation.AnimationRunner;
import waldonsm.connect4.gui.animation.FallingBallAnimation;
import waldonsm.connect4.model.Connect4Model;
import waldonsm.connect4.model.Connect4ModelImpl;

/**
 * The code for the Connect 4 JPanel.  
 * @author Shawn Waldon
 *
 */
public class Connect4Panel extends JPanel {

	/**
	 * to shut up the eclipse warnings
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The color for Player 1 (human)'s pieces
	 */
	public static final Color PLAYER1_COLOR = Color.RED;
	/**
	 * The color for Player 2 (computer)'s pieces
	 */
	public static final Color PLAYER2_COLOR = Color.BLUE;

	/**
	 * The size of a piece
	 */
	public static final int PIECE_SIZE = 40;
	
	/**
	 * The code for game in progress
	 */
	public static final int WINNER_GAME_IN_PROGRESS = 0;
	
	/**
	 * The code for human player win
	 */
	public static final int HUMAN_PLAYER_WIN = 1;
	
	/**
	 * The code for computer player win
	 */
	public static final int COMPUTER_PLAYER_WIN = 2;

	private final Connect4ModelImpl model;
	private final BufferedImage drawingBuffer;
	private final Raster defaultRaster;
	private boolean gameWon = false;
	private final Connect4AI aiBot;
	private Animation anim;
	private int xForTempPiece = -1;
	private int winner = WINNER_GAME_IN_PROGRESS;
	private final AtomicBoolean canClick;

	/**
	 * Creates a new Connect4Panel, with the given number of rows, columns, allowing either the
	 * player or computer to go first.
	 * @param cols the number of columns
	 * @param rows the number of rows
	 * @param playerTurnFirst true if the human player goes first
	 * @param ai the ai bot for the game
	 */
	public Connect4Panel(int cols, int rows, boolean playerTurnFirst, Connect4AI ai){
		model = new Connect4ModelImpl(rows, cols);
		aiBot = ai;
		Dimension dim = new Dimension(cols * 50, rows * 50 + 60);
		canClick = new AtomicBoolean(playerTurnFirst);
		setPreferredSize(dim);
		setSize(dim);
		drawingBuffer = new BufferedImage(cols * 50, rows * 50, BufferedImage.TYPE_4BYTE_ABGR);
		drawGrid(cols, rows, drawingBuffer.createGraphics());
		defaultRaster = drawingBuffer.getData();
		MouseAdapter adapter = new Connect4MouseHandler();
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
	}
	
	private static void drawGrid(int cols, int rows, Graphics2D g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i <=cols; i++) {
			g.fillRect(i*50-3, 0, 7, rows*50);
		}
		for (int j = 0; j <=rows; j++) {
			g.fillRect(0, j*50-10, cols*50, 7);
		}
	}

	/**
	 * Paints the component.... most drawing stored in the BufferedImage as a buffer... duh
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (xForTempPiece != -1) {
			g2.setColor(PLAYER1_COLOR);
			g2.fillOval(xForTempPiece, 5, PIECE_SIZE, PIECE_SIZE);
		}
		g2.drawImage(drawingBuffer, 0, 60, this);
		if (anim != null) {
			anim.draw(g2);
		}
	}
	
	/**
	 * Resets the component for a new game
	 * @param playerTurnFirst true if the human player should go first
	 */
	public void clearForNewGame(boolean playerTurnFirst) {
		anim = null;
		model.clear();
		drawingBuffer.setData(defaultRaster);
		gameWon = false;
		xForTempPiece = -1;
		winner = WINNER_GAME_IN_PROGRESS;
		canClick.set(playerTurnFirst);
		repaint();
	}
	
	/**
	 * Returns the winner of the game.  Beware, this is cleared by clearForNewGame, so
	 * if it is needed after calling that, store it elsewhere.
	 * @return either <code>WINNER_GAME_IN_PROGRESS</code>, <code>HUMAN_PLAYER_WIN</code>, or <code>COMPUTER_PLAYER_WIN</code>
	 */
	public int getWinner() {
		return winner;
	}

	/**
	 * Adds the specified animation, and animates it
	 * @param animation the animation
	 * @param listener the listener to be called when the animation is done
	 */
	private void addAnimation(Animation animation, AnimationDoneListener listener) {
		if (anim != null) throw new IllegalStateException();
		anim = animation;
		final AtomicBoolean atomBool = new AtomicBoolean(false);
		AnimationRunner.runAnimation(animation, atomBool, listener);
		final AtomicBoolean firstTime = new AtomicBoolean(true);

		SwingUtilities.invokeLater(
				/**
				 * The new runnable for the drawing of the animation
				 */
				new Runnable() {
					public void run() {

						// first time thorough start the animation
						if (firstTime.get() && !atomBool.get()) {
							atomBool.set(true);
							firstTime.set(false);
						}
						// repaint during the animation and resubmit for execution if animation not finished
						if (atomBool.get()) {
							Connect4Panel.this.repaint();
							
							try {
								Thread.sleep(AnimationRunner.ANIMATION_DELAY);
							} catch (InterruptedException ex) {
								Thread.currentThread().interrupt();
							}
							SwingUtilities.invokeLater(this);
							return;
						}
					}
				});
	}


	/**
	 * The mouseListsener for the panel.  The drop region (the region the mouselistener doesn't
	 * ignore, is the top 50 pixels of the screen
	 * @author Shawn Waldon
	 *
	 */
	private class Connect4MouseHandler extends MouseAdapter {
		/**
		 * Record x position of mouse if in the upper "drop region"
		 */
		public void mouseMoved(MouseEvent me) {
			if (! canClick.get() || gameWon || anim != null) {
				xForTempPiece = -1;
			} else if (me.getY() > 50) {
				xForTempPiece = -1;
			} else {
				xForTempPiece = (me.getX() / 50) * 50 + 5;
			}
			Connect4Panel.this.repaint();
		}

		/**
		 * Record x position of mouse if in upper "drop region"
		 */
		public void mouseDragged(MouseEvent me) {
			if (! canClick.get() || gameWon || anim != null) {
				xForTempPiece = -1;
				return;
			}
			mouseMoved(me);
		}

		
		/**
		 * If mouse is in the drop region of the screen, then this method causes the animation to
		 * be created and started.
		 */
		public void mouseReleased(MouseEvent me) {
			if (! canClick.get() || gameWon || anim != null || xForTempPiece == -1)
				return;
			final int col = me.getX() / 50;
			final int row = model.addPiece(col, Connect4Model.PLAYER1_MARK);
			canClick.set(false);
			Animation anim = new FallingBallAnimation(xForTempPiece, 5, 8, 0.3, (model.getHeight() - row - 1) * 50 + 60, PLAYER1_COLOR);
			xForTempPiece = -1;

			addAnimation(anim, new AnimationDoneListener() {
				/**
				 * Called when the animation is complete.  This method draws the image to the buffer
				 * and shows the dialog if the player won.  Additionally, it starts the animation 
				 * for the computer's move
				 */
				public void animationDone(Animation anim) {

					// draw the final piece onto the BufferedImage
					Graphics2D g = drawingBuffer.createGraphics();
					g.setColor(anim.getColor());
					g.fillOval(anim.getFinalX(), anim.getFinalY() - 60, PIECE_SIZE, PIECE_SIZE);
					Connect4Panel.this.anim = null;
					Connect4Panel.this.repaint();


					if (model.isWin(col, row)) {

						xForTempPiece = -1;
						winner = HUMAN_PLAYER_WIN;
						repaint();
						JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, Connect4Panel.this);
						JOptionPane.showMessageDialog(frame, "You Win!");
						gameWon = true;

						return;
					}

					try {
						Thread.sleep(50);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					doComputerTurn();

				}
			});
	
		}

		/**
		 * This method computes the computer's move using the ai bot and then starts its animation.
		 */
		public void doComputerTurn() {
			final int col = aiBot.chooseColumn(model);
			final int row = model.addPiece(col, Connect4Model.PLAYER2_MARK);
			Animation anim = new FallingBallAnimation(col * 50 + 5, 5, 8, 0.3, (model.getHeight() - row - 1) * 50 + 60, PLAYER2_COLOR);
			canClick.set(true);
			repaint();
			
			addAnimation(anim, new AnimationDoneListener() {
				/**
				 * This method draws the computer's move to the buffer, and then checks if the 
				 * computer won
				 */
				public void animationDone(Animation anim) {

					// draw the final piece onto the BufferedImage
					Graphics2D g = drawingBuffer.createGraphics();
					g.setColor(anim.getColor());
					g.fillOval(anim.getFinalX(), anim.getFinalY() - 60, PIECE_SIZE, PIECE_SIZE);
					Connect4Panel.this.anim = null;
					Connect4Panel.this.repaint();


					if (model.isWin(col, row)) {
						xForTempPiece = -1;
						winner = COMPUTER_PLAYER_WIN;
						repaint();
						JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, Connect4Panel.this);
						JOptionPane.showMessageDialog(frame, "You Lose!");
						gameWon = true;
						return;
					}
				}
			});

		}
	}
}
