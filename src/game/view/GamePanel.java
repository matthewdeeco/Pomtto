package game.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.grid.*;
import game.utility.Dialog;

import javax.swing.*;
import connection.Connection;

public class GamePanel extends JPanel implements GameGridObserver {
	private Connection conn;
	private GameGrid gameGrid;
	private Timer gameLoopTimer;
	private JLabel cpLabel;
	
	public GamePanel(Connection conn) {
		this.conn = conn;
		setBackground(Color.BLACK);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel cpPanel = new JPanel();
		cpPanel.add(new JLabel("Current CP: "));
		cpLabel = new JLabel();
		cpPanel.add(cpLabel);
		
		gameGrid = new PlayerGrid(conn, this);
		add(gameGrid);
		add(cpPanel);
		gameLoopTimer = new Timer(70, new GameLoop()); 
	}
	
	public void start() {
		gameLoopTimer.start();
	}

	@Override
	public void gameOver() {
		gameGrid.setVisible(false);
		gameLoopTimer.stop();
	}

	@Override
	public void scoreChanged(Integer score) {
		cpLabel.setText(score.toString());
	}
	
	private class GameLoop implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameGrid.update();
			gameGrid.repaint();
			gameLoopTimer.start();
		}
	}
}