package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
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
	private final ServerSocket socket;

	private final Map<Socket, Thread> socketToThread = new HashMap<Socket, Thread>();
	private final Map<Socket, User> socketToUser = new HashMap<Socket, User>();
	private final Map<User, Socket> userToSocket = new HashMap<User, Socket>();
	private final Map<String, User> nameToUser = new HashMap<String, User>();
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

	public synchronized List<Match> idClient(Socket client, User user)
			throws SQLException, DirectoryException {
		if (socketToThread.containsKey(client)) {
			socketToUser.put(client, user);
			userToSocket.put(user, client);
			nameToUser.put(user.getName(), user);

			List<Match> matches = Server.persistence.getRunningMatches(user);
			userToMatches.put(user, matches);
			return matches;
		}

		throw new DirectoryException("cannot id client");
	}

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
				
				for(Match m : userToMatches.get(user)) {
					try {
						Server.persistence.saveMatch(m);
					} catch (SQLException e) {
						logger.log(Level.SEVERE, "Couldn't save match", e);
					}
				}
				
				userToMatches.remove(user);
			}
		}
	}

	public synchronized void addMatch(Socket client, Match match) {
		userToMatches.get(client).add(match);
	}

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

	public synchronized Socket getSocket(User user) {
		return userToSocket.get(user);
	}
	
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
}