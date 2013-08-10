package game;

import java.awt.FlowLayout;
import java.awt.Font;

import game.grid.*;
import game.utility.ImageFactory;

import javax.swing.*;

public class PlayArea extends JPanel implements GameGridObserver {
	private GameGridObserver observer;
	private GameGrid grid;
	private JLabel currentCp;
	
	public PlayArea(GameGrid grid, GameGridObserver observer, int avatarIndex) {
		this.grid = grid;
		this.observer = observer;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel avatar = new JLabel(ImageFactory.createAvatarImage("face", avatarIndex));
		infoPanel.add(avatar);
		currentCp = new JLabel("0");
		currentCp.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		infoPanel.add(currentCp);
		grid.setGameGridObserver(this);
		add(grid);
		add(infoPanel);
	}

	@Override
	public void gameOver() {
		observer.gameOver();
	}

	@Override
	public void scoreChanged() {
		currentCp.setText(grid.getScore().toString());
	}
	
	public void update() {
		grid.update();
		grid.repaint();
	}
}
