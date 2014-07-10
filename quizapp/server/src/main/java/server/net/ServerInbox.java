package server.net;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.ServerDirectory;
import server.persistence.IPersistence;
import common.entities.*;
import common.net.NetUtils;
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
	 * Server's directory.
	 */
	private static ServerDirectory serverDir;
	/**
	 * Database access.
	 */
	private static IPersistence persistence;

	/**
	 * Creates an instance.
	 * 
	 * @param client
	 *            client's socket
	 * @param serverDir
	 *            server's directory
	 * @param persistence
	 *            persistence instance
	 */
	public ServerInbox(final Socket client, ServerDirectory _serverDir,
			IPersistence _persistence) {
		this.client = client;

		if (serverDir == null) {
			serverDir = _serverDir;
		}
		if (persistence == null) {
			persistence = _persistence;
		}
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
	 */
	public static synchronized void process(MatchSearchStartRequest req,
			Socket client) {
		User user = serverDir.getUser(client);
		int revision = serverDir.getRevision(client);

		if (waitingUser == null) {
			// wait
			waitingUser = user;
			waitingClient = client;
			waitingRevision = revision;
		} else {
			// assert that both clients have the question locally available
			List<Question> questions = persistence.getQuestions(Math.max(
					waitingRevision, revision));
			List<Integer> questionIds = getQuestionIds(questions);

			// connect
			NetUtils.send(waitingClient, new MatchCreatedResponse(user,
					questionIds));
			NetUtils.send(client, new MatchCreatedResponse(waitingUser,
					questionIds));
			serverDir.startMatch(client, waitingClient, questions);

			// clear spot
			waitingUser = null;
		}
	}

	/**
	 * Process a MatchSearchCancelRequest.
	 * 
	 * @param req
	 */
	public static synchronized void process(MatchSearchCancelRequest req) {
		NetUtils.send(waitingClient, new MatchSearchCancelledResponse());
		waitingClient = null;
	}

	// =====================================================================
	// Processing requests
	// =====================================================================

	/**
	 * Process a UserAuthRequest.
	 * 
	 * @param req
	 */
	private void process(UserAuthRequest req) {
		// login or register user
		User user;
		if (req.wantsToRegister()) {
			user = persistence.registerUser(req.getUsername(),
					req.getPassword());
		} else {
			user = persistence.loginUser(req.getUsername(), req.getPassword());
		}

		// on success
		if (user != null) {
			// send user the list of other users
			NetUtils.send(client,
					new AuthResponse(true, serverDir.getActiveUsers()));
			// inform other users about the new client
			NetUtils.send(serverDir.getActiveSockets(),
					new UserListChangedResponse(true, user));
			// connect username and socket in the server directory
			serverDir.idClient(client, user, req.getRevision());
		} else {
			NetUtils.send(client, new AuthResponse(false, null));
		}
	}

	/**
	 * Called when a user logged out: he will be deleted from the server
	 * directory and online users' player list will be updated.
	 * 
	 * @param req
	 */
	private void process(UserLogoutRequest req) {
		User disconnectedUser = serverDir.getUser(client);
		serverDir.removeClient(client);
		NetUtils.send(serverDir.getActiveSockets(),
				new UserListChangedResponse(false, disconnectedUser));
	}

	/**
	 * Process a UserDataChangeRequest.
	 * 
	 * @param req
	 */
	private void process(UserDataChangeRequest req) {
		if (!persistence.changeUserCredentials(req.getNewUsername(),
				req.getNewPassword(), req.getNewPasswordConfirm())) {
			NetUtils.send(client, new ErrorResponse(
					"User data couldn't be saved!"));
		}
	}

	/**
	 * Process a ChallengeSendRequest.
	 * 
	 * @param req
	 */
	private void process(ChallengeSendRequest req) {
		User sendTo = serverDir.getUser(req.getOpponent());
		if (!NetUtils.send(serverDir.getSocket(sendTo),
				new ChallengeReceivedResponse(sendTo))) {
			NetUtils.send(client, new ErrorResponse("Couldn't send challenge!"));
		}
	}

	/**
	 * Process a ChallengeDenyRequest.
	 * 
	 * @param req
	 */
	private void process(ChallengeDenyRequest req) {
		User sendTo = serverDir.getUser(req.getOpponent());
		NetUtils.send(serverDir.getSocket(sendTo), new ChallengeDeniedResponse(
				sendTo));
	}

	/**
	 * Process a ChallengeAcceptRequest.
	 * 
	 * @param req
	 */
	private void process(ChallengeAcceptRequest req) {
		Socket opponent = serverDir.getOpponent(client);

		List<Question> questions = persistence
				.getQuestions(Math.max(serverDir.getRevision(client),
						serverDir.getRevision(opponent)));
		List<Integer> questionIds = getQuestionIds(questions);

		NetUtils.send(
				opponent,
				new MatchCreatedResponse(serverDir.getUser(client), questionIds));
		NetUtils.send(client,
				new MatchCreatedResponse(serverDir.getUser(req.getOpponent()),
						questionIds));

		serverDir.startMatch(client, opponent, questions);
	}

	/**
	 * Process a GetRankingsRequest.
	 * 
	 * @param req
	 */
	private void process(GetRankingsRequest req) {
		NetUtils.send(
				client,
				new RankingsResponse(persistence.getRankedUsers(
						req.getOffset(), req.getLength())));
	}

	/**
	 * Process a AnswerSubmitRequest.
	 * 
	 * @param req
	 */
	private void process(AnswerSubmitRequest req) {
		serverDir.saveAnswer(client, req.getIndex());
		NetUtils.send(serverDir.getOpponent(client),
				new OpponentAnswerResponse(req.getIndex()));
	}

	/**
	 * Process a MatchLeaveRequest.
	 * 
	 * @param req
	 */
	private void process(MatchLeaveRequest req) {
		serverDir.endMatch(client);
		NetUtils.send(client, new OpponentLeftResponse());
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
		try {
			while (true) {
				String json = NetUtils.read(client);

				// Matching...
				{ // ============================================================
					UserAuthRequest obj = NetUtils.fromJson(json,
							UserAuthRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserLogoutRequest obj = NetUtils.fromJson(json,
							UserLogoutRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserDataChangeRequest obj = NetUtils.fromJson(json,
							UserDataChangeRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					MatchSearchStartRequest obj = NetUtils.fromJson(json,
							MatchSearchStartRequest.class);
					if (obj != null) {
						process(obj, client);
						continue;
					}
				}

				{ // ============================================================
					MatchSearchCancelRequest obj = NetUtils.fromJson(json,
							MatchSearchCancelRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeSendRequest obj = NetUtils.fromJson(json,
							ChallengeSendRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeAcceptRequest obj = NetUtils.fromJson(json,
							ChallengeAcceptRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeDenyRequest obj = NetUtils.fromJson(json,
							ChallengeDenyRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					GetRankingsRequest obj = NetUtils.fromJson(json,
							GetRankingsRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					AnswerSubmitRequest obj = NetUtils.fromJson(json,
							AnswerSubmitRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					MatchLeaveRequest obj = NetUtils.fromJson(json,
							MatchLeaveRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}
			}
		} catch (RuntimeException e) {
			serverDir.removeClient(client);
		}
	}
}
