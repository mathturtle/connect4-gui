package waldonsm.connect4.gui.animation;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

public class AnimationRunner implements Runnable {

	public static final int ANIMATION_DELAY = 60;

	public static void runAnimation(Animation animation, AtomicBoolean controlBool,
			AnimationDoneListener finishAction) {
		new Thread(new AnimationRunner(animation, controlBool, finishAction)).start();
	}
	
	private final AtomicBoolean control;
	private final Animation animation;
	private final AnimationDoneListener listener;
	
	private AnimationRunner(Animation anim, AtomicBoolean ctrl, AnimationDoneListener l) {
		animation = anim;
		control = ctrl;
		listener = l;
	}
	
	public void run() {
		while (!control.get() && ! Thread.interrupted()) {
			try {
				Thread.sleep(ANIMATION_DELAY);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		while (!animation.isDone() && ! Thread.interrupted()) {
			try {
				Thread.sleep(ANIMATION_DELAY);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			animation.next();
		}
		control.set(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				listener.animationDone(animation);
			}
		});
	}

}
