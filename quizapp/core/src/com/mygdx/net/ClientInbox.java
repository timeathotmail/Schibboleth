package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.IGame;

import common.net.SocketReadException;
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
				String json;
				try {
					json = net.read(serverSocket);
				} catch (SocketReadException e) {
					if (!e.isSocketClosed()) {
						throw e;
					} else {
						game.onError("Missed server message");
						continue;
					}
				}

				// Matching...
				{ // ============================================================
					AuthResponse obj = net.fromJson(json, AuthResponse.class);
					if (obj != null) {
						game.onLogin(obj.isSuccess(), obj.getTimeLimit(), obj.getUsers(),
								obj.getRunningMatches(), obj.getChallenges());
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
						game.onChallengeDenied(obj.getChallenge());
						continue;
					}
				}

				{ // ============================================================
					ChallengeReceivedResponse obj = net.fromJson(json,
							ChallengeReceivedResponse.class);
					if (obj != null) {
						game.onChallengeReceived(obj.getChallenge());
						continue;
					}
				}

				{ // ============================================================
					ErrorResponse obj = net.fromJson(json, ErrorResponse.class);
					if (obj != null) {
						game.onError(obj.getMessage());
						continue;
					}
				}

				{ // ============================================================
					MatchCreatedResponse obj = net.fromJson(json,
							MatchCreatedResponse.class);
					if (obj != null) {
						game.onMatchStarted(obj.getMatch());
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
						game.onOpponentAnswered(obj.getMatchId(),
								obj.getIndex(), obj.answeredInTime());
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
			}
		} catch (SocketReadException e) {
			game.onConnectionLost();
		}
	}
}