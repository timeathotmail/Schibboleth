package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.entities.User;


public class ServerDirectory implements Runnable {
	private static final Logger logger = Logger.getLogger("ClientRegistration");
	private final ServerSocket socket;
	private final Map<Socket, Thread> clients = new HashMap<Socket, Thread>();
	private final Map<Socket, User> clientIds = new HashMap<Socket, User>();

	public ServerDirectory(ServerSocket socket) {
		this.socket = socket;
	}

	public void run() {
		while (true) {
			try {
				final Socket client = socket.accept();
				Thread thread = new Thread(new ServerInbox(client, this));
				thread.start();
				clients.put(client, thread);
				logger.log(Level.INFO, client + " accepted");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "error accepting client", e);
			}
		}
	}

	public void RemoveClient(Socket client) {
		clients.remove(client);
		clientIds.remove(client);
		logger.log(Level.INFO, client + " disconnected");
	}

	public void idClient(Socket client, User user) {
		clientIds.put(client, user);
		logger.log(Level.INFO, client + " is " + user.getName());
	}

	public Collection<User> getActiveUsers() {
		return clientIds.values();
	}

	public Collection<Socket> getActiveSockets() {
		return clientIds.keySet();
	}
}