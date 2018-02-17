package infrastructure.conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketConnection implements Connection {

	public SocketConnection(Socket socket) throws IOException {
		this.socket = socket;
		out = new ObjectOutputStream(this.socket.getOutputStream());
        in = new ObjectInputStream(this.socket.getInputStream());
	}
	
	private Socket socket;
	
	private ObjectOutputStream out;
	
	private ObjectInputStream in;

	@Override
	public ObjectInputStream in() {
		return in;
	}

	@Override
	public ObjectOutputStream out() {
		return out;
	}

	@Override
	public void close() throws IOException {
		in.close();
        out.close();
        socket.close();
	}
	
	@Override
	public boolean isAlive() {
		return !socket.isClosed();
	}
}
