package server;

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

import server.persistence.Persistence;
import common.entities.Match;
import common.entities.Question;
import common.entities.User;
import common.net.NetUtils;

/**
 * Runnable managing clients.
 * 
 * @author Tim Wiechers
 */
public class ServerDirectory implements Runnable {
	private static final Logger logger = Logger.getLogger("ServerDirectory");
	private final Persistence persistence;
	private final ServerSocket socket;

	private final Map<Socket, Thread> socketToThread = new HashMap<Socket, Thread>();
	private final Map<Socket, User> socketToUser = new HashMap<Socket, User>();
	private final Map<User, Socket> userToSocket = new HashMap<User, Socket>();
	private final Map<Socket, Integer> socketToRev = new HashMap<Socket, Integer>();
	private final Map<String, User> nameToUser = new HashMap<String, User>();

	private final Map<User, MatchFactory> userToMatch1 = new HashMap<User, MatchFactory>();
	private final Map<User, MatchFactory> userToMatch2 = new HashMap<User, MatchFactory>();
	private final Map<Socket, Socket> socketToSocket1 = new HashMap<Socket, Socket>();
	private final Map<Socket, Socket> socketToSocket2 = new HashMap<Socket, Socket>();

	/**
	 * Creates an instance.
	 * 
	 * @param socket
	 *            server's socket
	 */
	public ServerDirectory(ServerSocket socket, Persistence persistence,
			NetUtils net) {
		this.socket = socket;
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
				final Socket client = socket.accept(); // wait for new client
				synchronized (this) {
					Thread thread = new Thread(new ServerInbox(client));
					thread.start();
					socketToThread.put(client, thread);
					logger.log(Level.INFO, client + " accepted");
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, "error accepting client", e);
			}
		}
	}

	public synchronized void idClient(Socket client, User user, int revision) {
		if (socketToThread.containsKey(client)) {
			socketToUser.put(client, user);
			userToSocket.put(user, client);
			socketToRev.put(client, revision);
			nameToUser.put(user.getName(), user);
		}
	}

	public synchronized void removeClient(Socket client) {
		Thread thread = socketToThread.get(client);

		if (thread != null) {
			thread.interrupt();
			socketToThread.remove(client);
			socketToRev.remove(client);
			User user = socketToUser.get(client);
			socketToUser.remove(client);
			userToSocket.remove(user);
			nameToUser.remove(user.getName());
		}
	}

	public synchronized void startMatch(Socket client1, Socket client2,
			List<Question> questions) {
		User user1 = socketToUser.get(client1);
		User user2 = socketToUser.get(client2);
		MatchFactory factory = new MatchFactory(user1, user2, questions);
		userToMatch1.put(user1, factory);
		userToMatch2.put(user2, factory);
		socketToSocket1.put(client1, client2);
		socketToSocket2.put(client2, client1);
	}

	public synchronized void saveAnswer(Socket client, int answerIndex) {
		User user = socketToUser.get(client);
		if (user == null) {
			// TODO
			return;
		}

		MatchFactory factory = getMatchFactory(user);
		if (factory == null) {
			// TODO
			return;
		}

		factory.addAnswer(user, answerIndex);

		if (factory.isFinished()) {
			Match match = factory.getMatch();
			try {
				persistence.saveMatch(match);
				removeMatch(match);
			} catch (SQLException e) {
				// TODO backup match?
			}
		}
	}

	public synchronized void endMatch(Socket client) {
		User user = socketToUser.get(client);
		if (user == null) {
			// TODO
			return;
		}

		MatchFactory factory = getMatchFactory(user);
		if (factory == null) {
			// TODO
			return;
		}

		factory.forfeit(user);

		Match match = factory.getMatch();
		try {
			persistence.saveMatch(match);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		removeMatch(match);
	}

	private synchronized MatchFactory getMatchFactory(User user) {
		MatchFactory factory = userToMatch1.get(user);
		return factory != null ? factory : userToMatch2.get(user);
	}

	private synchronized void removeMatch(Match match) {
		userToMatch1.remove(match.getUser1());
		userToMatch2.remove(match.getUser2());
		socketToSocket1.remove(userToSocket.get(match.getUser1()));
		socketToSocket2.remove(userToSocket.get(match.getUser2()));
	}

	// === getters ===

	public synchronized Collection<User> getUsers() {
		return socketToUser.values();
	}

	public synchronized Collection<Socket> getSockets() {
		return socketToUser.keySet();
	}

	public synchronized User getUser(Socket client) {
		return socketToUser.get(client);
	}

	public synchronized User getUser(String username) {
		return nameToUser.get(username);
	}

	public synchronized int getRevision(Socket client) {
		return socketToRev.get(client);
	}

	public synchronized Socket getSocket(User user) {
		return userToSocket.get(user);
	}

	public synchronized Socket getOpponentSocket(Socket client) {
		Socket opp = socketToSocket1.get(client);
		return opp != null ? opp : socketToSocket2.get(client);
	}
}