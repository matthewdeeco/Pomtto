package connection;
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
			e.printStackTrace();
			return null;
		}
	}
	
	public void writeObject(Object obj) {
		try {
			out.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			s.close();
		} catch (IOException e) {
		}
	}
}
