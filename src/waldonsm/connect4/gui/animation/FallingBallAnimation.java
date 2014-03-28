package waldonsm.connect4.gui.animation;

import java.awt.Color;
import java.awt.Graphics2D;

import waldonsm.connect4.gui.Connect4Panel;

public class FallingBallAnimation implements Animation {
	
	private double y;
	private double v;
	private final double a;
	private final int finalX, finalY;
	private final Color color;

	public FallingBallAnimation(int x, int y, double v, double a,
			int finalY, Color color) {
		super();
		this.y = y;
		this.v = v;
		this.a = a;
		this.finalX = x;
		this.finalY = finalY;
		this.color = color;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		synchronized (this) {
			g.fillOval(finalX, (int) y, Connect4Panel.PIECE_SIZE, Connect4Panel.PIECE_SIZE);
		}
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getFinalX() {
		return finalX;
	}

	@Override
	public int getFinalY() {
		return finalY;
	}

	@Override
	public synchronized void next() {
		y += v;
		v += a;
	}
	
	@Override
	public boolean isDone() {
		double d1 = Math.abs(y - finalY);
		double d2 = Math.abs(y + v - finalY);
		return d1 < d2;
	}

	
}
