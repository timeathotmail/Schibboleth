package com.mygdx.net;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import com.mygdx.game.Game;

import common.net.NetUtils;
import common.net.requests.AnswerSubmitRequest;
import common.net.requests.ChallengeAcceptRequest;
import common.net.requests.ChallengeDenyRequest;
import common.net.requests.ChallengeSendRequest;
import common.net.requests.GetRankingsRequest;
import common.net.requests.MatchLeaveRequest;
import common.net.requests.MatchSearchCancelRequest;
import common.net.requests.MatchSearchStartRequest;
import common.net.requests.UserAuthRequest;
import common.net.requests.UserDataChangeRequest;
import common.net.requests.UserLogoutRequest;

/**
 * Class used to connect and send requests to the server.
 * 
 * @author Tim Wiechers
 */
public class Client {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Client");
	/**
	 * The server's socket
	 */
	private Socket serverSocket;

	private final NetUtils net;

	/**
	 * Creates an instance. Opens a socket connection and initiiates a thread
	 * running a ClientInbox.
	 * 
	 * @param game
	 * @throws ConfigurationException 
	 */
	public Client(Game game) throws ConfigurationException {
		net = NetUtils.getInstance();
		try {
			serverSocket = new Socket(net.IP, net.PORT);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "couldn't connect to server", e);
			game.onNoConnection();
		}
		new Thread(new ClientInbox(game, net, serverSocket)).start();
	}

	/**
	 * Requests a user registration.
	 * 
	 * @param username
	 *            desired username
	 * @param password
	 *            desired password
	 */
	public void register(String username, String password, int revision) {
		net.send(serverSocket, new UserAuthRequest(username, password,
				true, revision));
	}

	/**
	 * Requests a user login.
	 * 
	 * @param username
	 *            username
	 * @param password
	 *            password
	 */
	public void login(String username, String password, int revision) {
		net.send(serverSocket, new UserAuthRequest(username, password,
				false, revision));
	}

	/**
	 * Informs the server of a logout
	 * 
	 * @param username
	 *            username
	 */
	public void logout() {
		net.send(serverSocket, new UserLogoutRequest());
	}

	/**
	 * Requests a user data change.
	 * 
	 * @param username
	 *            new username
	 * @param pw1
	 *            new password
	 * @param pw2
	 *            new password confirmation
	 */
	public void changeUserData(String username, String pw1, String pw2) {
		net.send(serverSocket, new UserDataChangeRequest(username, pw1,
				pw2));
	}

	/**
	 * Requests the server to search for a match.
	 */
	public void searchMatch() {
		net.send(serverSocket, new MatchSearchStartRequest());
	}

	/**
	 * Requests the server to cancel the search for a match.
	 */
	public void cancelSearch() {
		net.send(serverSocket, new MatchSearchCancelRequest());
	}

	/**
	 * Requests the server to send another user a challenge.
	 * 
	 * @param username
	 *            receiving user
	 */
	public void sendChallenge(String opponent) {
		net.send(serverSocket, new ChallengeSendRequest(opponent));
	}

	/**
	 * Requests the server to start a match for the receiving user.
	 * 
	 * @param username
	 *            receiving user
	 * @param questionId
	 *            the first question's id
	 */
	public void acceptChallenge(String opponent, int questionId) {
		net.send(serverSocket, new ChallengeAcceptRequest(opponent,
				questionId));
	}

	/**
	 * Requests the server to notify another user about a denied challenge.
	 * 
	 * @param username
	 *            denied user
	 */
	public void denyChallenge(String opponent) {
		net.send(serverSocket, new ChallengeDenyRequest(opponent));
	}

	/**
	 * Requests the server to send rankings to the client.
	 * 
	 * @param offset
	 *            first rank
	 * @param length
	 *            amount of users
	 */
	public void requestRankings(int offset, int length) {
		net.send(serverSocket, new GetRankingsRequest(offset, length));
	}

	/**
	 * Requests the server to send the client's answer choice to his opponent.
	 * 
	 * @param answer
	 *            answer's index
	 */
	public void submitAnswer(int answer, int nextQuestionId) {
		net.send(serverSocket, new AnswerSubmitRequest(answer));
	}

	/**
	 * Requests the server to notify the client's opponent that the client left
	 * the game.
	 */
	public void leaveMatch() {
		net.send(serverSocket, new MatchLeaveRequest());
	}
}