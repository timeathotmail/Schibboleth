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

import common.entities.Match;
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
	private static final Logger logger = Logger.getLogger("ServerDirectory");
	/**
	 * The server's socket.
	 */
	private final ServerSocket socket;
	/**
	 * Mapping sockets to threads.
	 */
	private final Map<Socket, Thread> socketToThread = new HashMap<Socket, Thread>();
	/**
	 * Mapping sockets to users.
	 */
	private final Map<Socket, User> socketToUser = new HashMap<Socket, User>();
	/**
	 * Mapping users to sockets.
	 */
	private final Map<User, Socket> userToSocket = new HashMap<User, Socket>();
	/**
	 * Mapping usernames to users.
	 */
	private final Map<String, User> nameToUser = new HashMap<String, User>();
	/**
	 * Mapping users to lists of matches.
	 */
	private final Map<User, List<Match>> userToMatches = new HashMap<User, List<Match>>();

	/**
	 * Creates an instance.
	 * 
	 * @param socket
	 *            server's socket
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

	/**
	 * Called on client login, identifies the socket. Returns list of running
	 * matches.
	 * 
	 * @param client
	 *            the user's socket
	 * @param user
	 *            the logged in user
	 * @return list of running matches
	 * @throws SQLException
	 *             if running matches couldn't be fetched
	 * @throws DirectoryException
	 *             if the client is not in the directory
	 */
	public synchronized List<Match> idClient(Socket client, User user)
			throws SQLException, DirectoryException {
		if (socketToThread.containsKey(client)) {
			socketToUser.put(client, user);
			userToSocket.put(user, client);
			nameToUser.put(user.getName(), user);

			List<Match> matches = Server.data.getRunningMatches(user);
			userToMatches.put(user, matches);
			return matches;
		}

		throw new DirectoryException("cannot id client");
	}

	/**
	 * Removes a client.
	 * 
	 * @param client
	 *            the client to remove
	 */
	public synchronized void removeClient(Socket client) {
		Thread thread = socketToThread.get(client);

		if (thread != null) {
			thread.interrupt();
			socketToThread.remove(client);
			User user = socketToUser.get(client);

			if (user != null) {
				socketToUser.remove(client);
				userToSocket.remove(user);
				nameToUser.remove(user.getName());

				for (Match m : userToMatches.get(user)) {
					try {
						Server.data.saveMatch(m);
					} catch (SQLException e) {
						logger.log(Level.SEVERE, "Couldn't save match", e);
					}
				}

				userToMatches.remove(user);
			}
		}
	}

	/**
	 * Adds a new match.
	 * 
	 * @param client
	 *            the client that started the match
	 * @param match
	 *            the new match
	 */
	public synchronized void addMatch(Socket client, Match match) {
		userToMatches.get(client).add(match);
	}

	/**
	 * Saves an answer for a given client and match.
	 * 
	 * @param client
	 *            the answering client
	 * @param matchId
	 *            the rowid of the match
	 * @param answerIndex
	 *            the answer's index
	 * @throws DirectoryException
	 *             if the user or match cannot be resolved
	 */
	public synchronized void saveAnswer(Socket client, int matchId,
			int answerIndex) throws DirectoryException {
		User user = socketToUser.get(client);
		if (user == null) {
			throw new DirectoryException(
					"cannot save answer: no user mapped to socket");
		}

		Match match = getMatch(user, matchId);

		if (match == null) {
			throw new DirectoryException(
					"cannot save answer: no match with id " + matchId);
		}

		match.addAnswer(user, answerIndex);
	}

	/**
	 * Returns the match with the given rowid.
	 * 
	 * @param user
	 *            the involved user
	 * @param id
	 *            the rowid of the match
	 * @return the match with the given rowid
	 */
	private Match getMatch(User user, int id) {
		Match match = null;
		for (Match m : userToMatches.get(user)) {
			if (m.getRowId() == id) {
				match = m;
				break;
			}
		}

		return match;
	}

	/**
	 * Returns the user's match opponent's socket if that user is online
	 * 
	 * @param client
	 *            the requesting client
	 * @param matchId
	 *            the rowid of the match the client is playing
	 * @return the socket or null if the opponent is offline
	 */
	public Socket getOpponentSocket(Socket client, int matchId) {
		User user = getUser(client);

		if (user == null) {
			return null;
		}

		Match match = getMatch(user, matchId);

		if (match == null) {
			return null;
		}

		User opponent = match.getUser1();
		return opponent != user ? userToSocket.get(opponent) : userToSocket
				.get(match.getUser2());
	}

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

	public synchronized Socket getSocket(User user) {
		return userToSocket.get(user);
	}
}