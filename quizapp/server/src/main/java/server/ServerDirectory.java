package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.persistence.Data;
import common.entities.User;

/**
 * Runnable managing clients.
 * 
 * @author Tim Wiechers
 */
public class ServerDirectory implements Runnable {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("ClientRegistration");
	/**
	 * Server's socket.
	 */
	private final ServerSocket socket;
	/**
	 * Mapping sockets and threads accepting messages on these sockets.
	 */
	private final Map<Socket, Thread> clients = new HashMap<Socket, Thread>();
	/**
	 * Mapping sockets to users.
	 */
	private final Map<Socket, User> clientIds = new HashMap<Socket, User>();

	/**
	 * Creates an instance
	 * @param socket server's socket
	 */
	public ServerDirectory(ServerSocket socket) {
		this.socket = socket;
	}

	/**
	 * Listens for socket connections, stores them and initiiates threads 
	 * running ServerInboxes.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				final Socket client = socket.accept();
				Thread thread = new Thread(new ServerInbox(client, this, Data.getInstance()));
				thread.start();
				clients.put(client, thread);
				logger.log(Level.INFO, client + " accepted");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "error accepting client", e);
			}
		}
	}

	/**
	 * Removes a client from the directory.
	 * @param client the client to remove
	 */
	public void removeClient(Socket client) {
		clients.get(client).interrupt();
		clients.remove(client);
		clientIds.remove(client);
		logger.log(Level.INFO, client + " disconnected");
	}

	/**
	 * Maps a user to a client socket.
	 * @param client the client
	 * @param user the identity of the client
	 */
	public void idClient(Socket client, User user) {
		clientIds.put(client, user);
		logger.log(Level.INFO, client + " is " + user.getName());
	}

	/**
	 * @return active users
	 */
	public Collection<User> getActiveUsers() {
		return clientIds.values();
	}

	/**
	 * @return active sockets
	 */
	public Collection<Socket> getActiveSockets() {
		return clientIds.keySet();
	}
}