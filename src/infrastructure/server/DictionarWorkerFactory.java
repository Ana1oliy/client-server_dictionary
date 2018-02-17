package infrastructure.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import dictionary.Dictionary;
import dictionary.commands.DictionaryCommand;
import infrastructure.Reader;
import infrastructure.conection.Connection;

public class DictionarWorkerFactory implements WorkerFactory {
	
	public DictionarWorkerFactory(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	private Dictionary dictionary;
	
	@Override
	public Runnable createWorker(Connection connection, ExecutorService executor) {
		return new Runnable() {
			
			final Reader<DictionaryCommand> reader = new Reader<> (connection.in(), this::onGetCommand);
			
			private void onGetCommand(DictionaryCommand command) {
				sendMessage(command.run(dictionary));
				
				if (connection.isAlive()) {
					executor.execute(reader);
				}
			}
				
			private void sendMessage(String message) {
				try {
					connection.out().writeObject(message);
				} catch (IOException e) {
					System.out.println("Can not write message");
					e.printStackTrace();
				}
			}
			
			@Override
			public void run() {
				executor.execute(reader);
			}
		};
	}
}
