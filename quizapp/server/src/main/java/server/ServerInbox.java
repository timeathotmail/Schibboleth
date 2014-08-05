package server;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	 * 
	 */
	private static int waitingRevision;

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
		int revision = Server.serverDir.getRevision(client);

		if (waitingUser == null) {
			// wait
			waitingUser = user;
			waitingClient = client;
			waitingRevision = revision;
		} else {
			// assert that both clients have the questions locally available
			List<Question> questions;
			try {
				questions = Server.persistence.getQuestions(Math.max(
						waitingRevision, revision));

				List<Integer> questionIds = getQuestionIds(questions);

				// connect
				Server.net.send(waitingClient, new MatchCreatedResponse(user,
						questionIds));
				Server.net.send(client, new MatchCreatedResponse(waitingUser,
						questionIds));
				Server.serverDir.startMatch(client, waitingClient, questions);

				// clear spot
				waitingUser = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				Server.net.send(client, new AuthResponse(false, null));
				return;
			}

			// send user the list of other users
			Server.net.send(client,
					new AuthResponse(true, Server.serverDir.getUsers()));
			// inform other users about the new client
			Server.net.send(Server.serverDir.getSockets(),
					new UserListChangedResponse(true, user));
			// connect username and socket in the server directory
			Server.serverDir.idClient(client, user, req.getRevision());

		} catch (SQLException e) {
			Server.net.send(client, new AuthResponse(false, null));
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
		User disconnectedUser = Server.serverDir.getUser(client);
		Server.serverDir.removeClient(client);
		Server.net.send(Server.serverDir.getSockets(),
				new UserListChangedResponse(false, disconnectedUser));
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
		Socket opponent = Server.serverDir.getOpponentSocket(client);

		List<Question> questions;
		try {
			questions = Server.persistence.getQuestions(Math.max(
					Server.serverDir.getRevision(client),
					Server.serverDir.getRevision(opponent)));

			List<Integer> questionIds = getQuestionIds(questions);

			Server.net.send(opponent,
					new MatchCreatedResponse(Server.serverDir.getUser(client),
							questionIds));
			Server.net.send(
					client,
					new MatchCreatedResponse(Server.serverDir.getUser(req
							.getOpponent()), questionIds));

			Server.serverDir.startMatch(client, opponent, questions);
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
		Server.serverDir.saveAnswer(client, req.getIndex());
		Server.net.send(Server.serverDir.getOpponentSocket(client),
				new OpponentAnswerResponse(req.getIndex()));
	}

	/**
	 * Process a MatchLeaveRequest.
	 * 
	 * @param req
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	private void process(MatchLeaveRequest req)
			throws IllegalArgumentException, SocketWriteException {
		Server.serverDir.endMatch(client);
		Server.net.send(client, new OpponentLeftResponse());
	}

	// =====================================================================
	// Utils
	// =====================================================================

	private static List<Integer> getQuestionIds(List<Question> questions) {
		List<Integer> questionIds = new ArrayList<Integer>();
		for (Question q : questions) {
			questionIds.add(q.getId());
		}
		return questionIds;
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
				if (!e.isSocketClosed()) {
					Server.serverDir.removeClient(client);
					return;
				} else {
					// TODO missed message
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
					MatchLeaveRequest obj = Server.net.fromJson(json,
							MatchLeaveRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}
			} catch (SocketWriteException e) {
				// TODO
			}
		}
	}
}
