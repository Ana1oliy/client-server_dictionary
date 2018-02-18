package infrastructure.conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Connection {
	
	ObjectInputStream in();
	
	ObjectOutputStream out();
	
	void close() throws IOException;
	
	boolean isAlive();
}
