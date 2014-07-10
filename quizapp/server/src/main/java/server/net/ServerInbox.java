package server.net;

import java.net.Socket;
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
// TODO: MatchStartRequest -> serverDir.startMatch
public class ServerInbox implements Runnable {
	
	private static Socket waitingClient;
	private static User waitingUser;
	private static int waitingRevision;

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
			List<Integer> questionIds = persistence.getQuestionIds(Math.max(
					waitingRevision, revision));

			// connect
			NetUtils.send(waitingClient, new MatchCreatedResponse(user,
					questionIds));
			NetUtils.send(client, new MatchCreatedResponse(waitingUser,
					questionIds));
			serverDir.startMatch(client, waitingClient, user, waitingUser,
					questionIds);

			// clear spot
			waitingUser = null;
		}
	}

	public static synchronized void process(MatchSearchCancelRequest req) {
		NetUtils.send(waitingClient, new MatchSearchCancelledResponse());
		waitingClient = null;
	}

	// =====================================================================

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

	/**
	 * TODO
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
	 * TODO
	 * 
	 * @param req
	 */
	private void process(UserDataChangeRequest req) {
		persistence.changeUserCredentials(req.getNewUsername(),
				req.getNewPassword(), req.getNewPasswordConfirm());
		// TODO send error if not successful
	}

	/**
	 * TODO
	 * 
	 * @param req
	 */
	private void process(ChallengeSendRequest req) {
		// serverDir.addChallenge(client, req.getOpponent());
		// TODO inform other client about challenge
		// TODO send error & delete challenge if not successful
	}

	/**
	 * TODO
	 * 
	 * @param req
	 */
	private void process(ChallengeAcceptRequest req) {
		// serverDir.startMatch(req.getOpponent(), req.getQuestionId());
		// TODO inform other client about challenge acceptance, send him first
		// question
	}

	/**
	 * TODO
	 * 
	 * @param req
	 */
	private void process(ChallengeDenyRequest req) {
		// serverDir.removeChallenge(req.getOpponent());
		// TODO inform other client about challenge denial
	}

	/**
	 * TODO
	 * 
	 * @param req
	 */
	private void process(GetRankingsRequest req) {
		// TODO send rankings
	}

	/**
	 * TODO
	 * 
	 * @param req
	 */
	private void process(AnswerSubmitRequest req) {
		serverDir.saveAnswer(client, req.getIndex());
		NetUtils.send(serverDir.getOpponent(client),
				new OpponentAnswerResponse(req.getIndex()));
	}

	/**
	 * TODO
	 * 
	 * @param req
	 */
	private void process(MatchLeaveRequest req) {
		serverDir.endMatch(client);
		// TODO inform opponent that the other user left
	}

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
