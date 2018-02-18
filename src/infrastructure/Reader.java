package infrastructure;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

public class Reader<T> implements Runnable {
	
	public Reader(ObjectInputStream in, Consumer<T> onRead) {
		this.in = in;
		this.completionHandler = new OnReadHandler<T>(onRead);
	}
	
	private ObjectInputStream in;
	
	private CompletionHandler<T, Void> completionHandler;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			completionHandler.completed((T) in.readObject(), null);
		} catch (EOFException e) {
            // Stream closed
        } catch (SocketException e) {
        	// Socket error. Suppose that socket normally closed. But this is not always true
        } catch (ClassNotFoundException e) {
        	completionHandler.failed(e, null);
        } catch (IOException e) {
        	completionHandler.failed(e, null);
		} catch (Throwable e) {
			completionHandler.failed(e, null);
		}
	}
	
	
	private static class OnReadHandler<T> implements CompletionHandler<T, Void> {

		public OnReadHandler(Consumer<T> handler) {
			this.handler = handler;
		}
		
		private Consumer<T> handler;
		
		@Override
		public void completed(T result, Void attachment) {
			handler.accept(result);
		}

		@Override
		public void failed(Throwable exc, Void attachment) {
			System.out.println("Failed to read input data");
			exc.printStackTrace();
		}
		
	}
	
}
