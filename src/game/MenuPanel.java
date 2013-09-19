package game;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.*;
import game.audio.AudioHandler;
import game.utility.ImageFactory;
import javax.swing.*;

public class MenuPanel extends JPanel {
	private GameWindow gameWindow;
	private JPanel helpPanel;
	private JButton randomMatchButton;
	private ImageIcon titleImage;
	
	public MenuPanel(GameWindow gameWindow) {
		this.gameWindow = gameWindow; 
		titleImage = ImageFactory.createImage("title.png");
		setLayout(new BorderLayout());
		
		JLabel titleLabel = new JLabel();
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 200, 10));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		randomMatchButton = new JButton("Random Match");
		randomMatchButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		buttonPanel.add(randomMatchButton);
		buttonPanel.setOpaque(false);
		addButtonToPanel(buttonPanel, "Help", new HelpListener());
		addButtonToPanel(buttonPanel, "Mute", new MuteListener());
		addButtonToPanel(buttonPanel, "Quit", new QuitListener());
		
		add(titleLabel, BorderLayout.PAGE_START);
		add(buttonPanel, BorderLayout.CENTER);
		helpPanel = new HelpPanel(gameWindow);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		titleImage.paintIcon(this, g, 0, 0);
	}
	
	private void addButtonToPanel(JPanel panel, String text, ActionListener listener) {
		JButton button  = new JButton(text);
		button.addActionListener(listener);
		button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		panel.add(button);
	}

	
	private class HelpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AudioHandler.playClickEffect();
			gameWindow.setContentPane(helpPanel);
		}
	}
	
	private class QuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AudioHandler.playClickEffect();
			System.exit(0);
		}
	}
	
	private class MuteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AudioHandler.toggleMute();			
		}
	}

	public JButton getRandomMatchButton() {
		return randomMatchButton;
	}	
}
