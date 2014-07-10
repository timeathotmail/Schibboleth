package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.IGame;

import common.net.NetUtils;
import common.net.responses.*;

/**
 * Runnable processing incoming server messages.
 * 
 * @author Tim Wiechers
 */
public class ClientInbox implements Runnable {
	/**
	 * Server socket.
	 */
	private final Socket serverSocket;
	/**
	 * Client's game instance.
	 */
	private final IGame game;

	/**
	 * Creates an instance.
	 * 
	 * @param game
	 *            client's game instance
	 * @param serverSocket
	 *            server socket
	 */
	public ClientInbox(IGame game, Socket serverSocket) {
		this.game = game;
		this.serverSocket = serverSocket;
	}

	/**
	 * Waits for a server message, then tries to map and process it.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				String json = NetUtils.read(serverSocket);

				// Matching...
				{ // ============================================================
					AuthResponse obj = NetUtils.fromJson(json,
							AuthResponse.class);
					if (obj != null) {
						game.onLogin(obj.isSuccess(), obj.getUsers());
						continue;
					}
				}

				{ // ============================================================
					UserListChangedResponse obj = NetUtils.fromJson(json,
							UserListChangedResponse.class);
					if (obj != null) {
						game.onUserListChanged(obj.hasConnected(),
								obj.getUser());
						continue;
					}
				}

				{ // ============================================================
					ChallengeDeniedResponse obj = NetUtils.fromJson(json,
							ChallengeDeniedResponse.class);
					if (obj != null) {
						game.onChallengeDenied(obj.getUser());
						continue;
					}
				}

				{ // ============================================================
					ChallengeReceivedResponse obj = NetUtils.fromJson(json,
							ChallengeReceivedResponse.class);
					if (obj != null) {
						game.onChallengeReceived(obj.getUser());
						continue;
					}
				}

				{ // ============================================================
					ErrorResponse obj = NetUtils.fromJson(json,
							ErrorResponse.class);
					if (obj != null) {
						game.onError(obj.getMessage());
						continue;
					}
				}

				{ // ============================================================
					MatchCreatedResponse obj = NetUtils.fromJson(json,
							MatchCreatedResponse.class);
					if (obj != null) {
						game.onMatchStarted(obj.getUser(), obj.getQuestionIds());
						continue;
					}
				}

				{ // ============================================================
					MatchSearchCancelledResponse obj = NetUtils.fromJson(json,
							MatchSearchCancelledResponse.class);
					if (obj != null) {
						game.onSearchCancelled();
						continue;
					}
				}

				{ // ============================================================
					OpponentAnswerResponse obj = NetUtils.fromJson(json,
							OpponentAnswerResponse.class);
					if (obj != null) {
						game.onOpponentAnswered(obj.getIndex());
						continue;
					}
				}

				{ // ============================================================
					OpponentLeftResponse obj = NetUtils.fromJson(json,
							OpponentLeftResponse.class);
					if (obj != null) {
						game.onOpponentLeft();
						continue;
					}
				}

				{ // ============================================================
					RankingsResponse obj = NetUtils.fromJson(json,
							RankingsResponse.class);
					if (obj != null) {
						game.onRankingsReceived(obj.getUsers());
						continue;
					}
				}
			}
		} catch (RuntimeException e) {
			game.onConnectionLost();
		}
	}
}