package infrastructure.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dictionary.commands.AddCommand;
import dictionary.commands.DictionaryCommand;
import infrastructure.Reader;
import infrastructure.conection.Connection;
import infrastructure.conection.SocketConnection;

public class SocketClient<I, O> {

	public SocketClient(String host, int port) throws UnknownHostException, IOException{
		connection = createConntction(host, port);
		reader = new Reader<>(connection.in(), this::onRead);
		incomingQueue = new LinkedList<>();
		outgoingQueue = new LinkedList<>();
		executor = Executors.newFixedThreadPool(2);
		runned = true;
		closed = false;
		run();
	}
	
	private Connection connection;
	
	private Reader<I> reader;
	
	private boolean runned;
	
	private boolean closed;
	
	private Queue<I> incomingQueue;
	
	private Queue<O> outgoingQueue;
	
	private ExecutorService executor;
	
	public boolean hasIncomingMessage() {
		return !incomingQueue.isEmpty();
	}
	
	public I getIncomingMessage() {
		return incomingQueue.poll();
	}
	
	public List<I> getAllIncomingMessages() {
		return new ArrayList<I>(incomingQueue);
	}
	
	public void sendMessage(O message) {
		outgoingQueue.add(message);
	}
	
	public void sendMessages(Collection<O> messages) {
		outgoingQueue.addAll(messages);
	}
	
	public boolean allMessagesSent() {
		return outgoingQueue.isEmpty();
	}
	
	public void stop() {
		runned = false;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	private void run() {
		runned = true;
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				executor.execute(reader);
				
				while (runned) {
					sendMessages();
				}
				
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				closed = true;
			}
		});
	}
	
	private void onRead(I message) {
		incomingQueue.add(message);
		if (runned) {
			executor.execute(reader);
		}
	}
	
	private void sendMessages() {
		synchronized (outgoingQueue) {
			while (!outgoingQueue.isEmpty()) {
				O message = outgoingQueue.poll();
				try {
					connection.out().writeObject(message);
				} catch (IOException e) {
					System.out.println(String.format("Can not to seend message \"%s.\"", message));
					e.printStackTrace();
				}
			}
		}
	}
	
	private static Connection createConntction(String host, int port) throws UnknownHostException, IOException {
		Socket socket = new Socket(host, port);
		return new SocketConnection(socket);
	}

	public static void main(String[] args) {
		try {
			DictionaryCommand command = new AddCommand("hello", Arrays.asList(new String[] {"привет"}));
			SocketClient<String, DictionaryCommand> client = new SocketClient<>("127.0.0.1", 3883);
			client.sendMessage(command);
			
			while (!client.hasIncomingMessage()) {
				TimeUnit.SECONDS.sleep(1);
			}
			
			List<String> incoming = client.getAllIncomingMessages();
			incoming.forEach(message -> System.out.println(message));
			client.stop();
			System.in.read();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}