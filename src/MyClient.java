import game.Client;

import javax.swing.SwingUtilities;


public class MyClient {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Client();
			}
		});
	}
}
