package server;

import game.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
	private static final int DEFAULT_PORT = 8888;
	private Connection waiting;
	private ServerSocket serverSocket;
	private boolean isRunning;

	public Server() {
		new ServerWindow(this, DEFAULT_PORT);
	}

	public void openPort(int portNo) throws IOException {
		serverSocket = new ServerSocket(portNo);
		isRunning = true;
		new Thread(new ServerThread()).start();
	}

	public void stop() {
		try {
			isRunning = false;
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ServerThread implements Runnable {
		@Override
		public void run() {
			while (isRunning) {
				try {
					Socket socket = serverSocket.accept();
					Connection conn = new Connection(socket);
					ClientHandler handler = new ClientHandler(conn);
					new Thread(handler).start();
				} catch (SocketException e) {
					// normal, server socket was closed
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ClientHandler implements Runnable {
		Connection conn;
		
		ClientHandler(Connection conn) throws IOException {
			this.conn = conn;
		}
		
		@Override
		public void run() {
			String msg = (String)conn.readObject(); // wait for client's message
			if (msg.equals("connect")) {
				if (waiting == null)
					waiting = conn;
				else
					matchup(waiting, conn);
			}
		}
		
	}

	private void matchup(Connection conn1, Connection conn2) {
		try {
			conn1.writeObject("serve");
		} catch (Exception e) {
			waiting = conn2;
			return;
		}
		try {
			conn2.writeObject("connect " + conn1.getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			waiting = null;
		}
	}
}
