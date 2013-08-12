package game;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.grid.*;
import game.utility.ImageFactory;

import javax.swing.*;

public class PlayArea extends JPanel implements GridObserver, ActionListener {
	private GameGrid grid;
	private JLabel currentCp;
	
	public PlayArea(GameGrid grid, int avatarIndex) {
		this.grid = grid;
		grid.addGridObserver(this);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel avatar = new JLabel(ImageFactory.createAvatarImage("face", avatarIndex));
		infoPanel.add(avatar);
		currentCp = new JLabel("0");
		currentCp.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		infoPanel.add(currentCp);
		
		add(grid);
		add(infoPanel);
		
		JButton debugButton = new JButton("debug");
		debugButton.addActionListener(this);
		infoPanel.add(debugButton);
	}

	@Override
	public void gameOver() {
	}

	@Override
	public void scoreChanged(int score) {
		currentCp.setText(String.valueOf(score));
	}
	
	public void update() {
		grid.update();
		grid.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		grid.toggleDebug();
	}
}
