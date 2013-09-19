package server;

import game.utility.Dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ServerWindow {
	private Server server;
	private JFrame frame;
	private JTextField port;
	private JButton startButton;
	private boolean running;

	public ServerWindow(Server server, Integer defaultPort) {
		this.server = server;
		frame = new JFrame("Pomtto Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(20, 20);
		
		port = new JTextField(7);
		port.setText(defaultPort.toString());
		
		JPanel portPanel = new JPanel();
		portPanel.add(new JLabel("Port: "));
		portPanel.add(port);
		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		frame.getContentPane().add(portPanel, BorderLayout.WEST);
		frame.getContentPane().add(startButton, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (running) { // stop
				server.stop();
				port.setEnabled(true);
				startButton.setText("Start");
				running = false;
			} else { // start
				try {
					int portNo = Integer.valueOf(port.getText());
					server.openPort(portNo);
					port.setEnabled(false);
					startButton.setText("Stop");
					running = true;
				} catch (Exception ex) {
					Dialog.errorMessage("Error opening port!");
				}
			}
		}
	}
}
