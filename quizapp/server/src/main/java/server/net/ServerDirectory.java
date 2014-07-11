package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.MatchFactory;
import server.persistence.Persistence;
import common.entities.Match;
import common.entities.Question;
import common.entities.User;

/**
 * Runnable managing clients.
 * 
 * @author Tim Wiechers
 */
public class ServerDirectory implements Runnable {

	/**
	 * BiMap: both players in a match act as key for an instance of
	 * MatchFactory.
	 */
	class MatchMap {
		private final Map<User, MatchFactory> matches1 = new HashMap<User, MatchFactory>();
		private final Map<User, MatchFactory> matches2 = new HashMap<User, MatchFactory>();
		private final Map<Socket, Socket> sockets1 = new HashMap<Socket, Socket>();
		private final Map<Socket, Socket> sockets2 = new HashMap<Socket, Socket>();

		public void put(Socket client1, Socket client2, User user1, User user2,
				List<Question> questions) {
			MatchFactory factory = new MatchFactory(user1, user2, questions);
			matches1.put(user1, factory);
			matches2.put(user2, factory);
			sockets1.put(client1, client2);
			sockets2.put(client2, client1);
		}

		public MatchFactory get(User user) {
			MatchFactory factory = matches1.get(user);
			return factory != null ? factory : matches2.get(user);
		}

		public Socket getOpponent(Socket client) {
			Socket opp = sockets1.get(client);
			return opp != null ? opp : sockets2.get(client);
		}

		public void remove(User user1, User user2) {
			matches1.remove(user1);
			matches2.remove(user2);
		}
	}

	// =====================================================================
	
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("ServerDirectory");
	/**
	 * Persistence.
	 */
	private final Persistence persistence;
	/**
	 * Server's socket.
	 */
	private final ServerSocket socket;
	
	/**
	 * Creates an instance.
	 * 
	 * @param socket
	 *            server's socket
	 */
	public ServerDirectory(ServerSocket socket, Persistence persistence) {
		this.socket = socket;
		this.persistence = persistence;
	}

	// =====================================================================
	// Mapping
	// =====================================================================

	/**
	 * Mapping sockets and threads accepting messages on these sockets.
	 */
	private final Map<Socket, Thread> threads = new HashMap<Socket, Thread>();
	/**
	 * Mapping sockets to users.
	 */
	private final Map<Socket, User> users = new HashMap<Socket, User>();
	/**
	 * Mapping sockets to user's app revision.
	 */
	private final Map<Socket, Integer> revisions = new HashMap<Socket, Integer>();
	/**
	 * 
	 */
	private final Map<String, User> usersFromName = new HashMap<String, User>();
	/**
	 * Running matches.
	 */
	private final MatchMap matches = new MatchMap();
	/**
	 * 
	 */
	private final Map<User, Socket> socketsFromUser = new HashMap<User, Socket>();
	
	/**
	 * Listens for socket connections, stores them and initiiates threads
	 * running ServerInboxes.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				final Socket client = socket.accept(); // wait for new client
				Thread thread = new Thread(new ServerInbox(client, this,
						persistence));
				thread.start();
				threads.put(client, thread);
				logger.log(Level.INFO, client + " accepted");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "error accepting client", e);
			}
		}
	}
	
	/**
	 * Maps a user to a client socket.
	 * 
	 * @param client
	 *            the client
	 * @param user
	 *            the identity of the client
	 */
	public synchronized void idClient(Socket client, User user, int revision) {
		users.put(client, user);
		revisions.put(client, revision);
		usersFromName.put(user.getName(), user);
		socketsFromUser.put(user, client);
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
		threads.get(client).interrupt();
		threads.remove(client);
		usersFromName.remove(users.get(client));
		socketsFromUser.remove(users.get(client));
		users.remove(client);
		revisions.remove(client);

		try {
			client.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error closing client socket", e);
		}

		logger.log(Level.INFO, client + " disconnected");
	}

	public synchronized Collection<User> getActiveUsers() {
		return users.values();
	}

	public synchronized Collection<Socket> getActiveSockets() {
		return users.keySet();
	}

	public User getUser(Socket client) {
		return users.get(client);
	}

	public User getUser(String username) {
		return usersFromName.get(username);
	}

	public int getRevision(Socket client) {
		return revisions.get(client);
	}

	public Socket getOpponent(Socket client) {
		return matches.getOpponent(client);
	}
	
	public Socket getSocket(User user) {
		return socketsFromUser.get(user);
	}

	// =====================================================================
	// Match organization
	// =====================================================================
	
	public void startMatch(Socket client1, Socket client2,
			List<Question> questions) {
		matches.put(client1, client2, users.get(client1),
				users.get(client2), questions);
	}

	public void saveAnswer(Socket client, int answerIndex) {
		User user = users.get(client);
		MatchFactory factory = matches.get(user);

		factory.addAnswer(user, answerIndex);

		if (factory.isFinished()) {
			Match match = factory.getMatch();
			try {
				persistence.saveMatch(match);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
			matches.remove(match.getUser1(), match.getUser2());
		}
	}

	public void endMatch(Socket client) {
		User user = users.get(client);
		if (user != null) {

			MatchFactory factory = matches.get(user);
			if (factory != null) {

				factory.forfeit(user);

				Match match = factory.getMatch();
				try {
					persistence.saveMatch(match);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
				}
				matches.remove(match.getUser1(), match.getUser2());
			}
		}
	}
}