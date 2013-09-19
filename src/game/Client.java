package game;

import java.io.IOException;
import java.net.Socket;

import game.audio.AudioHandler;
import game.utility.Dialog;

import javax.swing.*;

public class Client {
	public static final String SERVER = "localhost";
	public static final int SERVER_PORT = 8888;
	public static final int PORT = 7777;
	
	private GameWindow window;
	
	public Client() {
		AudioHandler.initialize();
		tryToInitializeLookAndFeel();
		
		ServerPortDialog spd = new ServerPortDialog(SERVER, SERVER_PORT);
		Socket server = spd.getSocket();
		try {
			Connection conn = new Connection(server);
			if (conn != null) {
				window = new GameWindow(conn);
				window.setVisible(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Try to initialize the look and feel. Exit if it failed. */
	private void tryToInitializeLookAndFeel() {
		try {
			initializeLookAndFeel();
		} catch (Exception ex) {
			Dialog.errorMessage("Failed in loading the look and feel!\nExiting...");
			System.exit(-1);
		}
	}

	/**
	 * Sets the look and feel to the system's look and feel.
	 * If that fails, then sets the look and feel to Java's.
	 */
	private void initializeLookAndFeel() throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
	}
}