package server;

import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.fabric.xmlrpc.Client;

import common.entities.*;
import common.net.SocketReadException;
import common.net.SocketWriteException;
import common.net.requests.*;
import common.net.responses.*;

/**
 * Runnable processing incoming client messages.
 * 
 * @author Tim Wiechers
 */
public class ServerInbox implements Runnable {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("ServerInbox");
	/**
	 * The client waiting to be matched with an opponent.
	 */
	private static Socket waitingClient;
	/**
	 * The user waiting to be matched with an opponent.
	 */
	private static User waitingUser;
	/**
	 * The client's socket.
	 */
	private final Socket client;

	/**
	 * Creates an instance.
	 * 
	 * @param client
	 *            the client whose messages will be processed
	 */
	public ServerInbox(final Socket client) {
		this.client = client;
	}

	// =====================================================================
	// Matchmaking
	// =====================================================================

	/**
	 * Process a MatchSearchStartRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @param client
	 *            a client who wants to be matched with an opponent
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} or {@link #waitingClient}is null
	 */
	private static synchronized void process(MatchSearchStartRequest req,
			Socket client) throws IllegalArgumentException,
			SocketWriteException {
		User user = Server.dir.getUser(client);

		if (waitingUser == null) { // user has to wait
			waitingUser = user;
			waitingClient = client;
		} else { // connect users
			try {
				Match match = new Match(user, waitingUser);
				Server.data.saveMatch(match); // set id & questions

				Server.net.send(waitingClient, new MatchCreatedResponse(match));
				Server.net.send(client, new MatchCreatedResponse(match));
				Server.dir.addMatch(client, match);
				Server.dir.addMatch(waitingClient, match);

				waitingUser = null; // clear spot
			} catch (SQLException e) {
				try {
					Server.net.send(client, new ErrorResponse(
							"Error connecting with " + waitingUser.getName()));
					Server.net.send(waitingClient, new ErrorResponse(
							"Error connecting with " + user.getName()));
				} catch (SocketWriteException e1) {
					logger.log(Level.SEVERE,
							"Cannot send user(s) error response", e1);
				}
			}
		}
	}

	/**
	 * Process a MatchSearchCancelRequest.
	 * 
	 * @param req
	 *            req the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private synchronized void process(MatchSearchCancelRequest req)
			throws IllegalArgumentException, SocketWriteException {
		if (client.equals(waitingClient)) {
			Server.net.send(waitingClient, new MatchSearchCancelledResponse());
			waitingClient = null;
		}
	}

	// =====================================================================
	// Processing requests
	// =====================================================================

	/**
	 * Process a UserAuthRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private void process(UserAuthRequest req) throws IllegalArgumentException,
			SocketWriteException {
		// login or register user
		try {
			User user = null;
			try {
				if (req.getRegister()) {
					user = Server.data.registerUser(req.getUsername(),
							req.getPassword());
				} else {
					user = Server.data.loginUser(req.getUsername(),
							req.getPassword());
				}
			} catch (IllegalArgumentException e) {
			}

			if (user == null) {
				Server.net.send(client, AuthResponse.Fail);
				return;
			}

			// send user the list of other users
			Server.net.send(client, new AuthResponse(true, Server.TIME_LIMIT,
					Server.dir.getUsers(), Server.data.getRunningMatches(user),
					Server.data.getChallenges(user)));
			// inform other users about the new client
			Server.net.send(Server.dir.getSockets(),
					new UserListChangedResponse(true, user));
			// connect username and socket in the server directory
			try {
				Server.dir.idClient(client, user);
			} catch (DirectoryException e) {
				Server.net.send(client, new ErrorResponse(e.getMessage()));
			}

		} catch (SQLException e) {
			Server.net.send(client, AuthResponse.Fail);
		}
	}

	/**
	 * Called when a user logged out: he will be deleted from the server
	 * directory and online users' player list will be updated.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private void process(UserLogoutRequest req)
			throws IllegalArgumentException, SocketWriteException {
		onUserLogout();
	}

	/**
	 * Process a UserDataChangeRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private void process(UserDataChangeRequest req)
			throws IllegalArgumentException, SocketWriteException {
		try {
			Server.data.changeUserCredentials(Server.dir.getUser(client),
					req.getNewUsername(), req.getNewPassword(),
					req.getNewPasswordConfirm());
		} catch (IllegalArgumentException e) {
			Server.net.send(client, new ErrorResponse("Bad input!"));
		} catch (SQLException e) {
			Server.net.send(client, new ErrorResponse(
					"User data couldn't be saved!"));
		}
	}

	/**
	 * Process a ChallengeSendRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 * @throws SQLException
	 *             if the challenge couldn't be stored in the database
	 */
	private void process(ChallengeSendRequest req)
			throws IllegalArgumentException, SocketWriteException, SQLException {
		Challenge c = req.getChallenge();
		c.setFrom(Server.dir.getUser(client));
		Server.data.saveChallenge(c); // set id
		Server.net.send(Server.dir.getSocket(c.getTo()),
				new ChallengeReceivedResponse(c));
	}

	/**
	 * Process a ChallengeDenyRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 * @throws SQLException
	 *             if the challenge couldn't be removed in the database
	 */
	private void process(ChallengeDenyRequest req)
			throws IllegalArgumentException, SocketWriteException, SQLException {
		Server.net.send(Server.dir.getSocket(req.getChallenge().getTo()),
				new ChallengeDeniedResponse(req.getChallenge()));
		Server.data.removeChallenge(req.getChallenge());
	}

	/**
	 * Process a ChallengeAcceptRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private void process(ChallengeAcceptRequest req)
			throws IllegalArgumentException, SocketWriteException {
		try {
			Match match = new Match(Server.dir.getUser(client), req
					.getChallenge().getFrom());
			Server.data.saveMatch(match); // set id & questions

			Server.net.send(client, new MatchCreatedResponse(match));
			Server.dir.addMatch(client, match);

			Socket opponent = Server.dir.getOpponentSocket(client,
					match.getRowId());
			if (opponent != null) { // opponent is online
				Server.net.send(opponent, new MatchCreatedResponse(match));
				Server.dir.addMatch(opponent, match);
			}

			Server.data.removeChallenge(req.getChallenge());
		} catch (SQLException e) {
			Server.net.send(client, new ErrorResponse(
					"Challenge couldn't be accepted!"));
		}
	}

	/**
	 * Process an AnswerSubmitRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private void process(AnswerSubmitRequest req)
			throws IllegalArgumentException, SocketWriteException {
		try {
			if (req.answeredInTime()) {
				Server.dir.saveAnswer(client, req.getMatchId(), req.getIndex());
			} else {
				Server.dir.saveAnswer(client, req.getMatchId(),
						Answer.NOT_ANSWERED_INDEX);
			}

		} catch (DirectoryException e) {
			Server.net.send(client, new ErrorResponse(e.getMessage()));
		}

		Socket opponent = Server.dir
				.getOpponentSocket(client, req.getMatchId());
		if (opponent != null) { // is online, sync
			Server.net.send(opponent,
					new OpponentAnswerResponse(req.getMatchId(),
							req.getIndex(), req.answeredInTime()));
		}
	}

	/**
	 * Process a UserSearchRequest.
	 * 
	 * @param req
	 *            the request to process
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 * @throws SocketWriteException
	 *             if a message couldn't be sent
	 * @throws SQLException
	 *             if the search query failed
	 */
	private void process(UserSearchRequest req)
			throws IllegalArgumentException, SocketWriteException, SQLException {
		Server.net.send(client,
				new UserSearchResponse(Server.data.getUser(req.getName())));
	}

	// =====================================================================
	// Utils
	// =====================================================================

	/**
	 * Handling a user logout.
	 * 
	 * @throws IllegalArgumentException
	 *             if {@link #client} is null
	 */
	private void onUserLogout() throws IllegalArgumentException {
		try {
			Server.net.send(
					Server.dir.getSockets(),
					new UserListChangedResponse(false, Server.dir
							.getUser(client)));
		} catch (Exception e1) {
			logger.log(Level.SEVERE, "cannot send UserListChangedResponse", e1);
		}

		Server.dir.removeClient(client);
	}

	// =====================================================================
	// Run loop/request matching
	// =====================================================================

	/**
	 * Waits for a client message, then tries to map and process it.
	 */
	public void run() {
		while (true) {
			String json;
			try {
				json = Server.net.read(client);
			} catch (SocketReadException e) {
				if (e.isSocketClosed()) {
					onUserLogout();
					return;
				} else {
					try {
						Server.net.send(client, new ErrorResponse(
								"Message couldn't be received!"));
					} catch (Exception e1) {
						logger.log(Level.SEVERE,
								"cannot send ErrorResponse to " + client, e);
					}

					continue;
				}
			}

			try {
				// Matching...
				{ // ============================================================
					UserAuthRequest obj = Server.net.fromJson(json,
							UserAuthRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserLogoutRequest obj = Server.net.fromJson(json,
							UserLogoutRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserDataChangeRequest obj = Server.net.fromJson(json,
							UserDataChangeRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					MatchSearchStartRequest obj = Server.net.fromJson(json,
							MatchSearchStartRequest.class);
					if (obj != null) {
						process(obj, client);
						continue;
					}
				}

				{ // ============================================================
					MatchSearchCancelRequest obj = Server.net.fromJson(json,
							MatchSearchCancelRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeSendRequest obj = Server.net.fromJson(json,
							ChallengeSendRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeAcceptRequest obj = Server.net.fromJson(json,
							ChallengeAcceptRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeDenyRequest obj = Server.net.fromJson(json,
							ChallengeDenyRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					AnswerSubmitRequest obj = Server.net.fromJson(json,
							AnswerSubmitRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserSearchRequest obj = Server.net.fromJson(json,
							UserSearchRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "cannot send response to " + client, e);
			}
		}
	}
}