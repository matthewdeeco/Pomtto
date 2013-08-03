package game.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import game.grid.*;
import javax.swing.*;

import connection.Connection;

public class GamePanel extends JPanel implements GameGridObserver {
	private Connection conn;
	private GameGrid playerGrid;
	private GameGrid opponentGrid;
	private Timer gameLoopTimer;
	private JLabel playerCpLabel;
	private JLabel opponentCpLabel;
	
	public GamePanel(Connection conn) {
		this.conn = conn;
		setBackground(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		
		JPanel cpPanel = new JPanel();
		cpPanel.add(new JLabel("Current CP: "));
		playerCpLabel = new JLabel();
		cpPanel.add(playerCpLabel);
		
		playerGrid = new PlayerGrid(conn, this);
		playerPanel.add(playerGrid);
		playerPanel.add(cpPanel);
		
		add(playerPanel);
		
		JPanel opponentPanel = new JPanel();
		opponentPanel.setLayout(new BoxLayout(opponentPanel, BoxLayout.Y_AXIS));

		cpPanel = new JPanel();
		cpPanel.add(new JLabel("Current CP: "));
		opponentCpLabel = new JLabel();
		cpPanel.add(opponentCpLabel);
		
		opponentGrid = new OpponentGrid(conn, this);
		opponentPanel.add(opponentGrid);
		opponentPanel.add(cpPanel);
		
		add(opponentPanel);
		gameLoopTimer = new Timer(70, new GameLoop()); 
	}
	
	public void start() {
		gameLoopTimer.start();
	}

	@Override
	public void gameOver() {
		gameLoopTimer.stop();
	}

	@Override
	public void scoreChanged(Integer score) {
		playerCpLabel.setText(score.toString());
	}
	
	private class GameLoop implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			playerGrid.update();
			playerGrid.repaint();
			opponentGrid.update();
			opponentGrid.repaint();
			gameLoopTimer.start();
		}
	}
}