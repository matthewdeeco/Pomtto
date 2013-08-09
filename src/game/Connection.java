package game;
import java.io.*;
import java.net.*;

public class Connection {
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Connection(Socket s) throws IOException {
		this.s = s;
		
		OutputStream os = s.getOutputStream();
		out = new ObjectOutputStream(os);
		out.flush();

		InputStream is = s.getInputStream();
		in = new ObjectInputStream(is);
	}
	
	public Object readObject() {
		try {
			return in.readObject();
		} catch (Exception e) {
			return null;
		}
	}
	
	public void writeObject(Object obj) throws Exception {
		out.writeObject(obj);
	}
	
	public void close() {
		try {
			s.close();
		} catch (IOException e) {
		}
	}
}
