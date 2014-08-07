package server;

import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private static final Logger logger = Logger.getLogger("ServerInbox");

	/**
	 * Client's socket.
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
	 * 
	 */
	private static Socket waitingClient;
	/**
	 * 
	 */
	private static User waitingUser;

	/**
	 * Process a MatchSearchStartRequest.
	 * 
	 * @param req
	 * @param client
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public static synchronized void process(MatchSearchStartRequest req,
			Socket client) throws IllegalArgumentException,
			SocketWriteException {
		User user = Server.serverDir.getUser(client);

		if (waitingUser == null) {
			// wait
			waitingUser = user;
			waitingClient = client;
		} else {
			List<Question> questions;
			try {
				questions = Server.persistence.getQuestions();
				Match match = new Match(user, waitingUser, questions, 0);
				Server.persistence.saveMatch(match);

				// connect
				Server.net.send(waitingClient, new MatchCreatedResponse(match));
				Server.net.send(client, new MatchCreatedResponse(match));
				Server.serverDir.addMatch(match);

				// clear spot
				waitingUser = null;
			} catch (SQLException e) {
				try {
					Server.net.send(client, new ErrorResponse(
							"Error connecting with " + waitingUser.getName()));
					Server.net.send(waitingClient, new ErrorResponse(
							"Error connecting with " + user.getName()));
				} catch (SocketWriteException _) {
				}
			}
		}
	}

	/**
	 * Process a MatchSearchCancelRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public static synchronized void process(MatchSearchCancelRequest req)
			throws IllegalArgumentException, SocketWriteException {
		Server.net.send(waitingClient, new MatchSearchCancelledResponse());
		waitingClient = null;
	}

	private void onUserLogout() {
		try {
			Server.net.send(Server.serverDir.getSockets(),
					new UserListChangedResponse(false,
							Server.serverDir.getUser(client)));
		} catch (Exception e1) {
			logger.log(Level.SEVERE,
					"cannot send UserListChangedResponse", e1);
		}
		
		Server.serverDir.removeClient(client);
	}
	
	// =====================================================================
	// Processing requests
	// =====================================================================

	/**
	 * Process a UserAuthRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(UserAuthRequest req) throws IllegalArgumentException,
			SocketWriteException {
		// login or register user
		try {
			User user = null;
			try {
				if (req.getRegister()) {
					user = Server.persistence.registerUser(req.getUsername(),
							req.getPassword());
				} else {
					user = Server.persistence.loginUser(req.getUsername(),
							req.getPassword());
				}
			} catch (IllegalArgumentException e) {
			}

			if (user == null) {
				Server.net.send(client, new AuthResponse(false, null, null));
				return;
			}

			// send user the list of other users
			Server.net.send(client,
					new AuthResponse(true, Server.serverDir.getUsers(),
							Server.persistence.getRunningMatches(user)));
			// inform other users about the new client
			Server.net.send(Server.serverDir.getSockets(),
					new UserListChangedResponse(true, user));
			// connect username and socket in the server directory
			try {
				Server.serverDir.idClient(client, user);
			} catch (DirectoryException e) {
				Server.net.send(client, new ErrorResponse(e.getMessage()));
			}

		} catch (SQLException e) {
			Server.net.send(client, new AuthResponse(false, null, null));
		}
	}

	/**
	 * Called when a user logged out: he will be deleted from the server
	 * directory and online users' player list will be updated.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(UserLogoutRequest req)
			throws IllegalArgumentException, SocketWriteException {
		onUserLogout();
	}

	/**
	 * Process a UserDataChangeRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(UserDataChangeRequest req)
			throws IllegalArgumentException, SocketWriteException {
		try {
			Server.persistence.changeUserCredentials(
					Server.serverDir.getUser(client), req.getNewUsername(),
					req.getNewPassword(), req.getNewPasswordConfirm());
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
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(ChallengeSendRequest req)
			throws IllegalArgumentException, SocketWriteException {
		User sendTo = Server.serverDir.getUser(req.getOpponent());
		Server.net.send(Server.serverDir.getSocket(sendTo),
				new ChallengeReceivedResponse(sendTo));

	}

	/**
	 * Process a ChallengeDenyRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(ChallengeDenyRequest req)
			throws IllegalArgumentException, SocketWriteException {
		User sendTo = Server.serverDir.getUser(req.getOpponent());
		Server.net.send(Server.serverDir.getSocket(sendTo),
				new ChallengeDeniedResponse(sendTo));
	}

	/**
	 * Process a ChallengeAcceptRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(ChallengeAcceptRequest req)
			throws IllegalArgumentException, SocketWriteException {
		// Socket opponent = Server.serverDir.getOpponentSocket(client);

		List<Question> questions;
		try {
			questions = Server.persistence.getQuestions();

			Match match = new Match(Server.serverDir.getUser(client),
					Server.serverDir.getUser(req.getOpponent()), questions, 0);

			Server.persistence.saveMatch(match);
			Server.net.send(client, new MatchCreatedResponse(match));

			Socket opponent = Server.serverDir.getOpponentSocket(client,
					match.getId());
			if (opponent != null) { // opponent is online
				Server.net.send(opponent, new MatchCreatedResponse(match));
			}

			Server.serverDir.addMatch(match);
		} catch (SQLException e) {
			Server.net.send(client, new ErrorResponse(
					"Challenge couldn't be accepted!"));
		}
	}

	/**
	 * Process a AnswerSubmitRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(AnswerSubmitRequest req)
			throws IllegalArgumentException, SocketWriteException {
		boolean answeredInTime = true; // TODO dont save answer in case not in
										// time
		try {
			Server.serverDir.saveAnswer(client, req.getMatchId(),
					req.getIndex());
		} catch (DirectoryException e) {
			Server.net.send(client, new ErrorResponse(e.getMessage()));
		}

		Socket opponent = Server.serverDir.getOpponentSocket(client,
				req.getMatchId());
		if (opponent != null) { // is online, sync
			Server.net.send(opponent,
					new OpponentAnswerResponse(req.getMatchId(),
							req.getIndex(), answeredInTime));
		}
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
			} catch (SocketWriteException e) {
				logger.log(Level.SEVERE, "cannot send response to " + client, e);
			}
		}
	}
}