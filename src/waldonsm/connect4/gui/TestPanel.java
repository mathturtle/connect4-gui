package waldonsm.connect4.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import waldonsm.connect4.ai.Connect4AI;
import waldonsm.connect4.ai.StupidAI;

public class TestPanel {

	/**
	 * This is a test main method to test the Connect4Panel it can be used as an example of how to
	 * use this class.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Connect4AI ai = new StupidAI();
				final Connect4Panel panel = new Connect4Panel(12,8,true,ai);
				JFrame frame = new JFrame();
				frame.add(panel);
				JButton button = new JButton("New Game");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						panel.clearForNewGame(true);
					}
				});
				JPanel p = new JPanel();
				p.add(button);
				frame.add(p, BorderLayout.SOUTH);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

}
