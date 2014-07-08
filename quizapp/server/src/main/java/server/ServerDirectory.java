package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.persistence.IPersistence;
import common.entities.Question;
import common.entities.User;

/**
 * Runnable managing clients.
 * 
 * @author Tim Wiechers
 */
// TODO: challenges
public class ServerDirectory implements Runnable {
	class MatchMap {
		private final Map<User, MatchFactory> matches1 = new HashMap<User, MatchFactory>();
		private final Map<User, MatchFactory> matches2 = new HashMap<User, MatchFactory>();

		public void put(User user1, User user2, Question[] questions) {
			MatchFactory factory = new MatchFactory(user1, user2, questions);
			matches1.put(user1, factory);
			matches2.put(user2, factory);
		}

		public MatchFactory get(User user) {
			MatchFactory factory = matches1.get(user);
			return factory != null ? factory : matches2.get(user);
		}
	}
	
	// =====================================================================

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
	 * Running matches.
	 */
	private final MatchMap matches = new MatchMap();
	private final Map<Socket, Integer> clientRevisions = new HashMap<Socket, Integer>();

	private final IPersistence persistence;
	private final MatchMaker matchMaker = new MatchMaker();

	/**
	 * Creates an instance
	 * 
	 * @param socket
	 *            server's socket
	 */
	public ServerDirectory(ServerSocket socket, IPersistence persistence) {
		this.socket = socket;
		this.persistence = persistence;
	}
	
	// =====================================================================

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

	// =====================================================================
	
	/**
	 * Maps a user to a client socket.
	 * 
	 * @param client
	 *            the client
	 * @param user
	 *            the identity of the client
	 */
	public synchronized void idClient(Socket client, User user, int revision) {
		clientIds.put(client, user);
		clientRevisions.put(client, revision);
		logger.log(Level.INFO, client + " is " + user.getName());
	}
	
	/**
	 * Removes a client from the directory.
	 * 
	 * @param client
	 *            the client to remove
	 */
	public synchronized void removeClient(Socket client) {
		endMatch(client);
		clients.get(client).interrupt();
		clients.remove(client);
		clientIds.remove(client);
		clientRevisions.remove(client);

		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.log(Level.INFO, client + " disconnected");
	}

	public synchronized Collection<User> getActiveUsers() {
		return clientIds.values();
	}

	public synchronized Collection<Socket> getActiveSockets() {
		return clientIds.keySet();
	}

	public User getUser(Socket client) {
		return clientIds.get(client);
	}

	public int getRevision(Socket client) {
		return clientRevisions.get(client);
	}

	// =====================================================================

	public void startMatch(Socket client, String challengingUser,
			int[] questionIds) {
		matches.put(clientIds.get(client),
				persistence.getUser(challengingUser),
				persistence.getQuestionsForGame(questionIds));
	}

	public void saveAnswer(Socket client, int answerIndex) {
		User user = clientIds.get(client);
		MatchFactory factory = matches.get(user);
		if (factory.addAnswer(user, answerIndex)) {
			persistence.saveMatch(factory.getMatch());
		}
	}

	public void endMatch(Socket client) {
		User user = clientIds.get(client);
		MatchFactory factory = matches.get(user);
		factory.forfeit(user);
		persistence.saveMatch(factory.getMatch());
	}
}