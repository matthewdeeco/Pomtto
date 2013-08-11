package game;

import java.awt.*;
import java.awt.event.*;
import game.grid.*;
import game.utility.AvatarChoiceDialog;
import game.utility.Dialog;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, GridObserver {
	private Timer gameLoopTimer;
	private PlayArea playerArea;
	private PlayArea opponentArea;
	
	public GamePanel(Connection conn) {
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		try {
			int playerAvatarIndex = new AvatarChoiceDialog().getSelectedAvatar();
			conn.writeObject(playerAvatarIndex);
			int opponentAvatarIndex = (Integer)conn.readObject();
			
			GameGrid playerGrid = new PlayerGrid(conn, playerAvatarIndex);
			playerGrid.addGridObserver(this);
			playerArea = new PlayArea(playerGrid, playerAvatarIndex);
			add(playerArea);
			
			GameGrid opponentGrid = new OpponentGrid(conn, opponentAvatarIndex);
			opponentGrid.addGridObserver(this);
			opponentArea = new PlayArea(opponentGrid, opponentAvatarIndex);
			add(opponentArea);

			gameLoopTimer = new Timer(70, new GameLoop());
			gameLoopTimer.setRepeats(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JButton pauseButton = new JButton("pause");
		pauseButton.addActionListener(this);
		add(pauseButton);
	}
	
	public void start() {
		gameLoopTimer.start();
	}

	public void gameOver() {
		gameLoopTimer.stop();
		Dialog.infoMessage("Game over!");
		// System.exit(0);
	}
	
	private class GameLoop implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			playerArea.update();
			opponentArea.update();
		}
	}

	@Override
	public void scoreChanged() {
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameLoopTimer.isRunning())
			gameLoopTimer.stop();
		else
			gameLoopTimer.start();
	}
}