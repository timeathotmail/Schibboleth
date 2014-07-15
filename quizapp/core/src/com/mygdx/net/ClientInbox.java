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

	private final NetUtils net;
	
	/**
	 * Creates an instance.
	 * 
	 * @param game
	 *            client's game instance
	 * @param serverSocket
	 *            server socket
	 */
	public ClientInbox(IGame game, NetUtils net, Socket serverSocket) {
		this.game = game;
		this.net = net;
		this.serverSocket = serverSocket;
	}

	/**
	 * Waits for a server message, then tries to map and process it.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				String json = net.read(serverSocket);

				// Matching...
				{ // ============================================================
					AuthResponse obj = net.fromJson(json,
							AuthResponse.class);
					if (obj != null) {
						game.onLogin(obj.isSuccess(), obj.getUsers());
						continue;
					}
				}

				{ // ============================================================
					UserListChangedResponse obj = net.fromJson(json,
							UserListChangedResponse.class);
					if (obj != null) {
						game.onUserListChanged(obj.hasConnected(),
								obj.getUser());
						continue;
					}
				}

				{ // ============================================================
					ChallengeDeniedResponse obj = net.fromJson(json,
							ChallengeDeniedResponse.class);
					if (obj != null) {
						game.onChallengeDenied(obj.getUser());
						continue;
					}
				}

				{ // ============================================================
					ChallengeReceivedResponse obj = net.fromJson(json,
							ChallengeReceivedResponse.class);
					if (obj != null) {
						game.onChallengeReceived(obj.getUser());
						continue;
					}
				}

				{ // ============================================================
					ErrorResponse obj = net.fromJson(json,
							ErrorResponse.class);
					if (obj != null) {
						game.onError(obj.getMessage());
						continue;
					}
				}

				{ // ============================================================
					MatchCreatedResponse obj = net.fromJson(json,
							MatchCreatedResponse.class);
					if (obj != null) {
						game.onMatchStarted(obj.getUser(), obj.getQuestionIds());
						continue;
					}
				}

				{ // ============================================================
					MatchSearchCancelledResponse obj = net.fromJson(json,
							MatchSearchCancelledResponse.class);
					if (obj != null) {
						game.onSearchCancelled();
						continue;
					}
				}

				{ // ============================================================
					OpponentAnswerResponse obj = net.fromJson(json,
							OpponentAnswerResponse.class);
					if (obj != null) {
						game.onOpponentAnswered(obj.getIndex());
						continue;
					}
				}

				{ // ============================================================
					OpponentLeftResponse obj = net.fromJson(json,
							OpponentLeftResponse.class);
					if (obj != null) {
						game.onOpponentLeft();
						continue;
					}
				}

				{ // ============================================================
					RankingsResponse obj = net.fromJson(json,
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