package server;

import game.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 8888;
	private Socket randomSocket;
	private ServerSocket serverSocket;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(PORT);
		
		print("Waiting for connections...");
		while(true) {
			Socket s = serverSocket.accept();
			if (randomSocket == null) {
				randomSocket = s;
			}
			else {
				new Matchup(randomSocket, s);
				randomSocket = null;
			}
		}
	}
	
	private void print(String message) {
		System.out.println(message);
	}
	
	private class Matchup {
		public Matchup(Socket s1, Socket s2) throws IOException {
			String inetAdd1 = s1.getInetAddress().getHostAddress();
			
			try {
				Connection conn1 = new Connection(s1);
				conn1.writeObject("serve");
				Connection conn2 = new Connection(s2);
				conn2.writeObject("connect " + inetAdd1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
