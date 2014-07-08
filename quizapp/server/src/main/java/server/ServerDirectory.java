package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.persistence.Data;
import server.persistence.IPersistence;
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
	 * Unanswered challenges.
	 */
	private final Map<User, User> challenges = new HashMap<User, User>(); // TODO List<pair<user>>
	/**
	 * Running matches.
	 */
	private final Map<User, User> matches = new HashMap<User, User>(); // TODO Map<pair<user>, RunningMatch>

	private final IPersistence persistence;
	private final MatchMaker matchMaker;

	/**
	 * Creates an instance
	 * 
	 * @param socket
	 *            server's socket
	 */
	public ServerDirectory(ServerSocket socket, MatchMaker matchMaker,
			IPersistence persistence) {
		this.socket = socket;
		this.matchMaker = matchMaker;
		this.persistence = persistence;
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
				Thread thread = new Thread(new ServerInbox(client, this,
						persistence, matchMaker));
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
	 * 
	 * @param client
	 *            the client to remove
	 */
	public void removeClient(Socket client) {
		clients.get(client).interrupt();
		clients.remove(client);
		clientIds.remove(client);
		User user = clientIds.get(client);
		challenges.remove(user);
		while (challenges.values().remove(user))
			;
		endMatch(client);

		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.log(Level.INFO, client + " disconnected");
	}

	/**
	 * Maps a user to a client socket.
	 * 
	 * @param client
	 *            the client
	 * @param user
	 *            the identity of the client
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

	public User getUser(Socket client) {
		return clientIds.get(client);
	}
	
	public void addChallenge(Socket client, String challengedUser) {
		// TODO
	}
	
	public void startMatch(String challengingUser, int questionId) {
		// TODO
	}
	
	public void removeChallenge(String challengingUser) {
		// TODO
	}
	
	public void saveAnswer(Socket client, int answerIndex, int nextQuestionId) {
		// TODO check if id == 0
		// TODO if last question save match in persistence, update user
		// statistics
	}

	public void endMatch(Socket client) {
		// TODO leaving client loses, save match in persistence
	}
}