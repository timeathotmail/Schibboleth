package com.mygdx.net;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.QuizGame;
import common.net.NetUtils;

/**
 * Base class used to connect and send requests to the server.
 * 
 * @author Tim Wiechers
 */
public abstract class AbstractClient {
	/**
	 * The server's socket
	 */
	protected Socket serverSocket;
	
	/**
	 * Creates an instance.
	 * Opens a socket connection and initiiates a thread running a ClientInbox.
	 * @param game
	 */
	protected AbstractClient(QuizGame game) {
		try {
			serverSocket = new Socket(NetUtils.IP, NetUtils.PORT);
			new Thread(new ClientInbox(game, serverSocket)).start();
		} catch (Exception e) {
			Gdx.app.error("Client", "couldn't connect to server", e);
			game.onNoConnection();
		}
	}
	
	/**
	 * Requests a user registration.
	 * @param username desired username
	 * @param password desired password
	 */
	abstract public void register(String username, String password);
	
	/**
	 * Requests a user login.
	 * @param username username
	 * @param password password
	 */
	abstract public void login(String username, String password);
	
	/**
	 * Informs the server of a logout
	 * @param username username
	 */
	abstract public void logout(String username);
	
	/**
	 * Requests a user data change.
	 * @param username new username
	 * @param pw1 new password
	 * @param pw2 new password confirmation
	 */
	abstract public void changeUserData(String username, String pw1, String pw2);
	
	/**
	 * Requests the server to search for a match.
	 */
	abstract public void searchMatch();
	
	/**
	 * Requests the server to cancel the search for a match.
	 */
	abstract public void cancelSearch();
	
	/**
	 * Requests the server to send another user a challenge.
	 * @param username receiving user
	 */
	abstract public void sendChallenge(String username);
	
	/**
	 * Requests the server to start a match for the receiving user.
	 * @param username receiving user
	 */
	abstract public void acceptChallenge(String username);
	
	/**
	 * Requests the server to notify another user about a denied challenge.
	 * @param username denied user
	 */
	abstract public void denyChallenge(String username);
	
	/**
	 * Requests the server to send rankings to the client.
	 * @param offset first rank
	 * @param length amount of users
	 */
	abstract public void requestRankings(int offset, int length);
	
	/**
	 * Requests the server to send the client's answer choice to his opponent.
	 * @param answer answer's index
	 */
	abstract public void submitAnswer(int answer, boolean correct);
	
	/**
	 * Requests the server to notify the client's opponent that the client left the game.
	 */
	abstract public void leaveMatch();
}