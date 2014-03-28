package waldonsm.connect4.gui.animation;

import java.awt.Color;
import java.awt.Graphics2D;

public interface Animation {

	/**
	 * Steps the animation to its next frame
	 */
	void next();
	
	/**
	 * Draws the current phase of the animation
	 * @param g the Graphics2D object
	 */
	void draw(Graphics2D g);
	
	/**
	 * The color the animation will be drawn in
	 * @return the color the animation will be drawn in
	 */
	Color getColor();
	
	/**
	 * Returns the final X position of the animation
	 * @return the final X position of the animation
	 */
	int getFinalX();
	
	/**
	 * Returns the final Y position of the animation
	 * @return the final Y position of the animation
	 */
	int getFinalY();
	
	/**
	 * Returns true if the animation is done
	 * @return true if the animation is done
	 */
	boolean isDone();
}
