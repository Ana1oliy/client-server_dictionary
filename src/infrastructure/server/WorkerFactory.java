package infrastructure.server;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;

import infrastructure.conection.Connection;

public interface WorkerFactory {
	Runnable createWorker(Connection connection, ExecutorService executor);
}
