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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dictionary.commands.AddCommand;
import dictionary.commands.DeleteCommand;
import dictionary.commands.DictionaryCommand;
import dictionary.commands.GetCommand;
import infrastructure.Reader;
import infrastructure.conection.Connection;
import infrastructure.conection.SocketConnection;

public class SocketClient<I, O> {

	public SocketClient(String host, int port) throws UnknownHostException, IOException{
		//connection = createConntction(host, port);
		
		incomingQueue = new ConcurrentLinkedQueue<>();
		outgoingQueue = new ConcurrentLinkedQueue<>();
		executor = Executors.newFixedThreadPool(2);
		this.host = host;
		this.port = port;
		run();
	}
	
	private String host;
	
	private int port;
	
	
	
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
		closed = false;
		
		executor.execute(new Runnable() {
			
			private Connection connection;
			
			private Reader<I> reader;
			
			@Override
			public void run() {
				try (Socket socket = new Socket(host, port)) {
					connection = new SocketConnection(socket);
					reader = new Reader<>(connection.in(), this::onRead);
					executor.execute(reader);
					
					while (runned) {
						sendMessages();
					}
					
					try {
						connection.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (UnknownHostException e) {
					System.out.println(String.format("Unknown host \"%s\"", host));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					closed = true;
					runned = false;
					executor.shutdown();
				}
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
		});
	}
	
	
	
//	private static Connection createConntction(String host, int port) throws UnknownHostException, IOException {
//		Socket socket = new Socket(host, port);
//		return new SocketConnection(socket);
//	}

	public static void main(String[] args) {
		String host;
		int port;
		String command;
		String key;
		Collection<String> words;
		DictionaryCommand dictionaryCommand = null;
		
		try {
			host = args[0];
			port = Integer.parseInt(args[1]);
			command = args[2];
			key = args[3];
			words = Arrays.asList(Arrays.copyOfRange(args, 4, args.length));
		} catch (Exception e) {
			wrongArgs();
			return;
		}
		
		switch (command) {
			case "add":
				dictionaryCommand = new AddCommand(key, words);
				break;
			case "get":
				dictionaryCommand = new GetCommand(key);
				break;
			case "delete":
				dictionaryCommand = new DeleteCommand(key, words);
				break;
			default:
				wrongArgs();
				return;
		}
		
		try {
			SocketClient<String, DictionaryCommand> client = new SocketClient<>(host, port);
			client.sendMessage(dictionaryCommand);
			
			while (!client.hasIncomingMessage()) {
				
			}
			
			List<String> incoming = client.getAllIncomingMessages();
			incoming.forEach(message -> System.out.println(message));
			client.stop();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static void wrongArgs() {
		System.out.println("Wrong execution arguments!");
		System.out.println("Use \"host port command(add|get|delete) key [words]\"");
	}
}