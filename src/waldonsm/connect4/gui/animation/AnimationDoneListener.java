package waldonsm.connect4.gui.animation;

/**
 * Allows specification of work to be done on the animation thread after the animation
 * is completed.
 * @author Shawn
 *
 */
public interface AnimationDoneListener {
	/**
	 * An method to be called by the animation thread after the animation is completed.
	 */
	void animationDone(Animation anim);
}
