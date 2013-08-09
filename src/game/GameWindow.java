package game;

import game.utility.Dialog;
import game.utility.ImageFactory;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import javax.swing.*;


public class GameWindow {
	private static final int WINDOW_WIDTH = 580;
	private static final int WINDOW_HEIGHT = 400;
	private JFrame frame;
	private JPanel menuPanel;
	private JButton randomMatchButton;
	
	public GameWindow() {
		frame = new JFrame("ポムっと");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(30, 30, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false);
		
		menuPanel = createMenuPanel();
		showMenu();
		randomMatchButton.doClick();
	}
	
	private JPanel createMenuPanel() {
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		
		JLabel titleLabel = new JLabel(ImageFactory.createImage("Sen.png"));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		randomMatchButton = new JButton("Random Match");
		randomMatchButton.addActionListener(new RandomMatchListener(randomMatchButton));
		randomMatchButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		buttonPanel.add(randomMatchButton);
		
		addButtonToPanel(buttonPanel, "Help", new HelpListener());
		addButtonToPanel(buttonPanel, "Quit", new QuitListener());
		
		menuPanel.add(titleLabel, BorderLayout.PAGE_START);
		menuPanel.add(buttonPanel, BorderLayout.CENTER);
		
		return menuPanel;
	}
	
	private void addButtonToPanel(JPanel panel, String text, ActionListener listener) {
		JButton button  = new JButton(text);
		button.addActionListener(listener);
		button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		panel.add(button);
	}
	
	private void showMenu() {
		AudioHandler.playMenuTrack();
		frame.setContentPane(menuPanel);
	} 
	
	private void startGame(Connection conn) {
		AudioHandler.playMainGameTrack();
		GamePanel gamePanel = new GamePanel(conn);
		frame.setContentPane(gamePanel);
		frame.pack();
		// frame.setVisible(true);
		gamePanel.start();
	}
	
	public void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}
	
	private class RandomMatchListener implements ActionListener {
		private JButton source;
		
		public RandomMatchListener(JButton source) {
			this.source = source;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				final Socket server = new Socket(Client.SERVER, Client.SERVER_PORT);
				source.setText("Waiting for opponent...");
				source.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Connection conn = findOpponent(server);
							source.setText("Starting game...");
							startGame(conn);
						} catch (IOException e) {
							connectionFailed();
						}
					}
				}).start();
			} catch (IOException ex) {
				connectionFailed();
			}
		}
		
		private Connection findOpponent(Socket server) throws IOException {
			Connection conn = new Connection(server);
			String msg = (String)conn.readObject();
			conn.close();
			
			Socket s;
			if (msg.equals("serve")) {
				ServerSocket ssocket = new ServerSocket(Client.PORT);
				s = ssocket.accept();
				ssocket.close();
			} else { // connect to the other player's server socket
				try {
					Thread.sleep(500); // give opponent time to set up server socket
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String targetHost = msg.split(" ")[1];
				s = new Socket(targetHost, Client.PORT);
			}
			return new Connection(s);
		}
		
		private void connectionFailed() {
			Dialog.errorMessage("Connection failed!");
			source.setText("Retry Connection");
			source.setEnabled(true);
		}
	}
	
	private class HelpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		}
	}
	
	private class QuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}