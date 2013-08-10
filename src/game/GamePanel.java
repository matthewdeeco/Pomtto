package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import game.grid.*;
import game.utility.AvatarChoiceDialog;
import game.utility.Dialog;

import javax.swing.*;

public class GamePanel extends JPanel implements GameGridObserver {
	private Connection conn;
	private Timer gameLoopTimer;
	private PlayArea playerArea;
	private PlayArea opponentArea;
	
	public GamePanel(Connection conn) {
		this.conn = conn;
		setBackground(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		try {
			int playerAvatarIndex = new AvatarChoiceDialog().getSelectedAvatar();
			conn.writeObject(playerAvatarIndex);
			int opponentAvatarIndex = (Integer)conn.readObject();
			
			playerArea = new PlayArea(new PlayerGrid(conn, playerAvatarIndex), this, playerAvatarIndex);
			add(playerArea);
			opponentArea = new PlayArea(new OpponentGrid(conn, opponentAvatarIndex), this, opponentAvatarIndex);
			add(opponentArea);

			gameLoopTimer = new Timer(70, new GameLoop());
		} catch (Exception e) {
			e.printStackTrace();
		} 
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
			gameLoopTimer.start();
		}
	}

	@Override
	public void scoreChanged() {
	}
}