package infrastructure.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dictionary.Dictionary;
import infrastructure.conection.Connection;
import infrastructure.conection.SocketConnection;

public class SocketServer {

	final public static int DEFAULT_PORT = 3883;
	
	final public static int DEFAULT_THREADS_COUNT = 4;
	
	public SocketServer(WorkerFactory workerFactory, int port) throws IOException {
		createSocket(port);
		createExecutorService(DEFAULT_THREADS_COUNT);
		this.workerFactory = workerFactory;
		reader = new BufferedReader(new InputStreamReader(System.in));
		run();
	}
	
	private void createSocket(int port) throws IOException {
		serverListener = new ServerSocket(port);
	}
	
	private void createExecutorService(int threadsCount) {
		executor = Executors.newFixedThreadPool(threadsCount);
	}
	
	private ServerSocket serverListener;
	
	private ExecutorService executor;
	
	private WorkerFactory workerFactory;
	
	private BufferedReader reader;
	
	private void run() {
		
		System.out.println("Use \"exit\" command to stop server");
		System.out.print("> ");
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				while (!serverListener.isClosed()) {
					try {
						Socket client = serverListener.accept();
						Connection newConnection = new SocketConnection(client);
						executor.execute(workerFactory.createWorker(newConnection, executor));
					} catch (IOException exc) {
						exc.printStackTrace();
					}
				}
				
				System.out.println("Stopped");
			}
		});
		
		try {
			while (true) {
				if (reader.ready()) {
	                String command = reader.readLine();
	                if (command.equalsIgnoreCase("exit")) {
	                    System.out.println("Stopping server...");
	                    stop();
	                    break;
	                } else {
	                	System.out.println("Unknown command");
	                }
	                System.out.print("> ");
	            }
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() throws IOException {
		if (!serverListener.isClosed()) {
			serverListener.close();
			executor.shutdown();
		}
	}
	
	public static void main(String[] args) {
		//SocketServer server;
		Dictionary dictionary = new Dictionary();
		WorkerFactory workerFactory = new DictionarWorkerFactory(dictionary);
		int port = DEFAULT_PORT;
		try {
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			new SocketServer(workerFactory, port);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
