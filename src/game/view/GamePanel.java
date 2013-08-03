package game.view;

import java.awt.*;
import game.grid.*;
import game.utility.ThreadSleep;

import javax.swing.*;
import connection.Connection;

public class GamePanel extends JPanel {
	private Connection conn;
	private GameGrid gameGrid;
	
	public GamePanel(Connection conn) {
		this.conn = conn;
		setBackground(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		gameGrid = new PlayerGrid(conn);
		add(gameGrid);
	}
	
	public void start() {
		while(true) {
			gameGrid.update();
			gameGrid.repaint();
			ThreadSleep.sleep(20);
		}
	}
}
