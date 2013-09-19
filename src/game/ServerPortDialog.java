package game;

import game.utility.Dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.*;

public class ServerPortDialog extends JDialog {
	private JTextField server;
	private JTextField port;
	private JButton connectButton;
	private Socket socket;
	
	public ServerPortDialog(String defaultServer, Integer defaultPort) {
		server = new JTextField(15);
		port = new JTextField(5);
		connectButton = new JButton("Connect");
		setLocation(60, 30);
		
		server.setText(defaultServer);
		port.setText(defaultPort.toString());
		
		server.addActionListener(new ConnectListener());
		port.addActionListener(new ConnectListener());
		connectButton.addActionListener(new ConnectListener());
		
		
		JPanel serverIpPanel = new JPanel();
		serverIpPanel.add(new JLabel("Server: "));
		serverIpPanel.add(server);
		
		JPanel portPanel = new JPanel();
		portPanel.add(new JLabel("Port: "));
		portPanel.add(port);
		
		JPanel portConnectPanel = new JPanel(new BorderLayout());
		portConnectPanel.add(portPanel, BorderLayout.WEST);
		portConnectPanel.add(connectButton, BorderLayout.CENTER);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(serverIpPanel);
		mainPanel.add(portConnectPanel);
		
		setTitle("Connect to Server");
		setContentPane(mainPanel);
		setModal(true);
		setAlwaysOnTop(true);
		pack();
		setVisible(true);
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	private class ConnectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				socket = new Socket(server.getText(), Integer.valueOf(port.getText()));
				dispose();
			} catch (Exception ex) {
				Dialog.errorMessage("Could not connect to server!");
			}
		}
	}

}
