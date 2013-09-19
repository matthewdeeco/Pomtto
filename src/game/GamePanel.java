package game;

import java.awt.*;
import java.awt.event.*;

import game.audio.AudioHandler;
import game.grid.*;
import game.grid.event.*;
import game.utility.Dialog;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, GridObserver {
	private Connection conn;
	private Timer gameLoopTimer;
	private PlayArea playerArea;
	private PlayArea opponentArea;
	private GameGrid playerGrid;
	private GameGrid opponentGrid;
	
	public GamePanel(Connection conn) {
		this.conn = conn;
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		try {
			int playerAvatarIndex = new AvatarChoiceDialog().getSelectedAvatar();
			conn.writeObject(playerAvatarIndex);
			int opponentAvatarIndex = (Integer)conn.readObject();
			
			playerGrid = new PlayerGrid(conn, playerAvatarIndex);
			playerGrid.addGridObserver(this);
			playerArea = new PlayArea(playerGrid, playerAvatarIndex);
			add(playerArea);
			
			opponentGrid = new OpponentGrid(conn, opponentAvatarIndex);
			opponentGrid.addGridObserver(this);
			opponentArea = new PlayArea(opponentGrid, opponentAvatarIndex);
			add(opponentArea);

			gameLoopTimer = new Timer(70, new GameLoop());
			gameLoopTimer.setRepeats(true);
			new Thread(new GridEventReceiver()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		gameLoopTimer.start();
	}

	public void gameOver() {
		if (gameLoopTimer.isRunning()) {
			gameLoopTimer.stop();
			Dialog.infoMessage("Game over!");
			AudioHandler.toggleMute();
			// System.exit(0);
		}
	}
	
	private class GameLoop implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			playerArea.update();
			opponentArea.update();
		}
	}

	@Override
	public void scoreChanged(int score) {
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameLoopTimer.isRunning())
			gameLoopTimer.stop();
		else
			gameLoopTimer.start();
	}
	
	private class GridEventReceiver implements Runnable {
		@Override
		public void run() {
			GridEvent event;
			while ((event = (GridEvent)conn.readObject()) != null) {
				if (event instanceof Attack)
					event.invoke(playerGrid);
				else
					event.invoke(opponentGrid);
			}
		}
	}
}