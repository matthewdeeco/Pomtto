package game;

import game.audio.AudioHandler;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class GameWindow {
	private static final int WINDOW_WIDTH = 440;
	private static final int WINDOW_HEIGHT = 400;
	private Connection server;
	private JFrame frame;
	private MenuPanel menuPanel;
	
	public GameWindow(Connection server) {
		this.server = server;
		
		frame = new JFrame("Pomtto");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(30, 30, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false);
		
		menuPanel = new MenuPanel(this);
		menuPanel.getRandomMatchButton().addActionListener(new RandomMatchListener(menuPanel.getRandomMatchButton()));

		AudioHandler.playMenuTrack();
		showMainMenu();
		// randomMatchButton.doClick();
	}
	
	public void showMainMenu() {
		frame.setContentPane(menuPanel);
		frame.setVisible(true);
	} 
	
	public void startGame(Connection conn) {
		AudioHandler.playMainGameTrack();
		GamePanel gamePanel = new GamePanel(conn);
		setContentPane(gamePanel);
		gamePanel.start();
	}
	
	public void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}
	
	public void setContentPane(JPanel contentPane) {
		frame.setContentPane(contentPane);
		((JComponent) frame.getContentPane()).revalidate();
		frame.repaint();
		frame.setVisible(true);
	}
	

	private class RandomMatchListener implements ActionListener {
		private JButton source;
		
		public RandomMatchListener(JButton source) {
			this.source = source;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			AudioHandler.playClickEffect();
			source.setText("Waiting for opponent...");
			source.setEnabled(false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Connection conn = findOpponent();
						source.setText("Starting game...");
						startGame(conn);
					} catch (Exception e) {
					}
				}
			}).start();
		}
		
		private Connection findOpponent() throws Exception {
			server.writeObject("connect");
			String msg = (String)server.readObject();
			System.out.println(msg);
			server.close();
			
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
	}
}