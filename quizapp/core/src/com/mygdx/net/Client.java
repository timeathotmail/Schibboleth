package com.mygdx.net;

import java.net.Socket;

import javax.naming.ConfigurationException;

import com.mygdx.game.IGame;

import common.net.NetUtils;
import common.net.SocketWriteException;
import common.net.requests.AnswerSubmitRequest;
import common.net.requests.ChallengeAcceptRequest;
import common.net.requests.ChallengeDenyRequest;
import common.net.requests.ChallengeSendRequest;
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
	public Client(IGame game, String ip, int port)
			throws ConfigurationException, NoConnectionException {
		net = NetUtils.getInstance();

		try {
			serverSocket = new Socket(ip, port);
		} catch (Exception e) {
			throw new NoConnectionException(e);
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
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void register(String username, String password, int revision)
			throws IllegalArgumentException, SocketWriteException {
		net.send(serverSocket, new UserAuthRequest(username, password, true,
				revision));
	}

	/**
	 * Requests a user login.
	 * 
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @throws SocketWriteException
	 */
	public void login(String username, String password, int revision)
			throws SocketWriteException {
		net.send(serverSocket, new UserAuthRequest(username, password, false,
				revision));
	}

	/**
	 * Informs the server of a logout
	 * 
	 * @param username
	 *            username
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void logout() throws IllegalArgumentException, SocketWriteException {
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
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void changeUserData(String username, String pw1, String pw2)
			throws IllegalArgumentException, SocketWriteException {
		net.send(serverSocket, new UserDataChangeRequest(username, pw1, pw2));
	}

	/**
	 * Requests the server to search for a match.
	 * 
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void searchMatch() throws IllegalArgumentException,
			SocketWriteException {
		net.send(serverSocket, new MatchSearchStartRequest());
	}

	/**
	 * Requests the server to cancel the search for a match.
	 * 
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void cancelSearch() throws IllegalArgumentException,
			SocketWriteException {
		net.send(serverSocket, new MatchSearchCancelRequest());
	}

	/**
	 * Requests the server to send another user a challenge.
	 * 
	 * @param username
	 *            receiving user
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void sendChallenge(String opponent) throws IllegalArgumentException,
			SocketWriteException {
		net.send(serverSocket, new ChallengeSendRequest(opponent));
	}

	/**
	 * Requests the server to start a match for the receiving user.
	 * 
	 * @param username
	 *            receiving user
	 * @param questionId
	 *            the first question's id
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void acceptChallenge(String opponent)
			throws IllegalArgumentException, SocketWriteException {
		net.send(serverSocket, new ChallengeAcceptRequest(opponent));
	}

	/**
	 * Requests the server to notify another user about a denied challenge.
	 * 
	 * @param username
	 *            denied user
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void denyChallenge(String opponent) throws IllegalArgumentException,
			SocketWriteException {
		net.send(serverSocket, new ChallengeDenyRequest(opponent));
	}

	/**
	 * Requests the server to send the client's answer choice to his opponent.
	 * 
	 * @param answer
	 *            answer's index
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void submitAnswer(int answer, float timeNeeded)
			throws IllegalArgumentException, SocketWriteException {
		net.send(serverSocket, new AnswerSubmitRequest(answer, timeNeeded));
	}

	/**
	 * Requests the server to notify the client's opponent that the client left
	 * the game.
	 * 
	 * @throws SocketWriteException
	 * @throws IllegalArgumentException
	 */
	public void leaveMatch() throws IllegalArgumentException,
			SocketWriteException {
		net.send(serverSocket, new MatchLeaveRequest());
	}
}