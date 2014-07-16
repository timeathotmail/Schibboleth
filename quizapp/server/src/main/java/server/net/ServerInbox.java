package server.net;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.persistence.Persistence;
import common.entities.*;
import common.net.SocketReadException;
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
	 * NetUtils instance.
	 */
	private static NetUtils net;
	/**
	 * Server's directory.
	 */
	private static ServerDirectory serverDir;
	/**
	 * Database access.
	 */
	private static Persistence persistence;

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
	public ServerInbox(NetUtils _net, final Socket client,
			ServerDirectory _serverDir, Persistence _persistence) {
		this.client = client;

		if (net == null) {
			net = _net;
		}

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
			// assert that both clients have the questions locally available
			List<Question> questions;
			try {
				questions = persistence.getQuestions(Math.max(waitingRevision,
						revision));

				List<Integer> questionIds = getQuestionIds(questions);

				// connect
				net.send(waitingClient, new MatchCreatedResponse(user,
						questionIds));
				net.send(client, new MatchCreatedResponse(waitingUser,
						questionIds));
				serverDir.startMatch(client, waitingClient, questions);

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
	 */
	public static synchronized void process(MatchSearchCancelRequest req) {
		net.send(waitingClient, new MatchSearchCancelledResponse());
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
		try {
			User user = null;
			try {
				if (req.getRegister()) {
					user = persistence.registerUser(req.getUsername(),
							req.getPassword());
				} else {
					user = persistence.loginUser(req.getUsername(),
							req.getPassword());
				}
			} catch (IllegalArgumentException e) {
			}

			if (user == null) {
				net.send(client, new AuthResponse(false, null));
				return;
			}

			// send user the list of other users
			net.send(client, new AuthResponse(true, serverDir.getActiveUsers()));
			// inform other users about the new client
			net.send(serverDir.getActiveSockets(), new UserListChangedResponse(
					true, user));
			// connect username and socket in the server directory
			serverDir.idClient(client, user, req.getRevision());

		} catch (SQLException e) {
			net.send(client, new AuthResponse(false, null));
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
		net.send(serverDir.getActiveSockets(), new UserListChangedResponse(
				false, disconnectedUser));
	}

	/**
	 * Process a UserDataChangeRequest.
	 * 
	 * @param req
	 */
	private void process(UserDataChangeRequest req) {
		try {
			persistence.changeUserCredentials(serverDir.getUser(client),
					req.getNewUsername(), req.getNewPassword(),
					req.getNewPasswordConfirm());
		} catch (IllegalArgumentException e) {
			net.send(client, new ErrorResponse("Bad input!"));
		} catch (SQLException e) {
			net.send(client, new ErrorResponse("User data couldn't be saved!"));
		}
	}

	/**
	 * Process a ChallengeSendRequest.
	 * 
	 * @param req
	 */
	private void process(ChallengeSendRequest req) {
		User sendTo = serverDir.getUser(req.getOpponent());
		if (!net.send(serverDir.getSocket(sendTo),
				new ChallengeReceivedResponse(sendTo))) {
			net.send(client, new ErrorResponse("Couldn't send challenge!"));
		}
	}

	/**
	 * Process a ChallengeDenyRequest.
	 * 
	 * @param req
	 */
	private void process(ChallengeDenyRequest req) {
		User sendTo = serverDir.getUser(req.getOpponent());
		net.send(serverDir.getSocket(sendTo), new ChallengeDeniedResponse(
				sendTo));
	}

	/**
	 * Process a ChallengeAcceptRequest.
	 * 
	 * @param req
	 */
	private void process(ChallengeAcceptRequest req) {
		Socket opponent = serverDir.getOpponent(client);

		List<Question> questions;
		try {
			questions = persistence.getQuestions(Math.max(
					serverDir.getRevision(client),
					serverDir.getRevision(opponent)));

			List<Integer> questionIds = getQuestionIds(questions);

			net.send(opponent,
					new MatchCreatedResponse(serverDir.getUser(client),
							questionIds));
			net.send(
					client,
					new MatchCreatedResponse(serverDir.getUser(req
							.getOpponent()), questionIds));

			serverDir.startMatch(client, opponent, questions);
		} catch (SQLException e) {
			net.send(client, new ErrorResponse(
					"Challenge couldn't be accepted!"));
		}
	}

	/**
	 * Process a GetRankingsRequest.
	 * 
	 * @param req
	 */
	private void process(GetRankingsRequest req) {
		try {
			net.send(
					client,
					new RankingsResponse(persistence.getRankedUsers(
							req.getOffset(), req.getLength())));
		} catch (Exception e) {
			net.send(client, new ErrorResponse("Can't get rankings!"));
		}
	}

	/**
	 * Process a AnswerSubmitRequest.
	 * 
	 * @param req
	 */
	private void process(AnswerSubmitRequest req) {
		serverDir.saveAnswer(client, req.getIndex());
		net.send(serverDir.getOpponent(client),
				new OpponentAnswerResponse(req.getIndex()));
	}

	/**
	 * Process a MatchLeaveRequest.
	 * 
	 * @param req
	 */
	private void process(MatchLeaveRequest req) {
		serverDir.endMatch(client);
		net.send(client, new OpponentLeftResponse());
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
				String json;
				try {
					json = net.read(client);
				} catch(SocketReadException e) {
					if(!e.isSocketClosed()) {
						throw e;
					} else {
						// TODO missed message
						continue;
					}
				}

				// Matching...
				{ // ============================================================
					UserAuthRequest obj = net.fromJson(json,
							UserAuthRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserLogoutRequest obj = net.fromJson(json,
							UserLogoutRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					UserDataChangeRequest obj = net.fromJson(json,
							UserDataChangeRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					MatchSearchStartRequest obj = net.fromJson(json,
							MatchSearchStartRequest.class);
					if (obj != null) {
						process(obj, client);
						continue;
					}
				}

				{ // ============================================================
					MatchSearchCancelRequest obj = net.fromJson(json,
							MatchSearchCancelRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeSendRequest obj = net.fromJson(json,
							ChallengeSendRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeAcceptRequest obj = net.fromJson(json,
							ChallengeAcceptRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					ChallengeDenyRequest obj = net.fromJson(json,
							ChallengeDenyRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					GetRankingsRequest obj = net.fromJson(json,
							GetRankingsRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					AnswerSubmitRequest obj = net.fromJson(json,
							AnswerSubmitRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}

				{ // ============================================================
					MatchLeaveRequest obj = net.fromJson(json,
							MatchLeaveRequest.class);
					if (obj != null) {
						process(obj);
						continue;
					}
				}
			}
		} catch (SocketReadException e) {
			serverDir.removeClient(client);
		}
	}
}
