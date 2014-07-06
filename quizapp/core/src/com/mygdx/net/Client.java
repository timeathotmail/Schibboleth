package com.mygdx.net;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.QuizGame;

import common.net.NetUtils;
import common.net.requests.*;

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
	
	/**
	 * Creates an instance.
	 * Opens a socket connection and initiiates a thread running a ClientInbox.
	 * @param game
	 */
	public Client(QuizGame game) {
		try {
			serverSocket = new Socket(NetUtils.IP, NetUtils.PORT);
			new Thread(new ClientInbox(game, serverSocket)).start();
		} catch (Exception e) {
			Gdx.app.error("Client", "couldn't connect to server", e);
			game.onNoConnection();
		}
	}
	
	/**
	 * Sends an AuthRequest to register.
	 * @param username desired username
	 * @param password desired password
	 */
	public void register(String username, String password) {
		NetUtils.send(serverSocket, new AuthRequest(username, password, true));
	}
	
	/**
	 * Sends an AuthRequest to login.
	 * @param username username
	 * @param password password
	 */
	public void login(String username, String password) {
		NetUtils.send(serverSocket, new AuthRequest(username, password, false));
	}
	
	/**
	 * Sends a LogoutRequest.
	 * @param username username
	 */
	public void logout(String username) {
		NetUtils.send(serverSocket, new LogoutRequest());
	}
	
	/**
	 * TODO
	 * @param username
	 * @param pw1
	 * @param pw2
	 */
	public void changeUserData(String username, String pw1, String pw2) {
	}
	
	/**
	 * TODO
	 */
	public void searchMatch() {
	}
	
	/**
	 * TODO
	 */
	public void cancelSearch() {
	}
	
	/**
	 * TODO
	 * @param username
	 */
	public void sendChallenge(String username) {
	}
	
	/**
	 * TODO
	 * @param username
	 */
	public void acceptChallenge(String username) {
	}
	
	/**
	 * TODO
	 * @param username
	 */
	public void denyChallenge(String username) {
	}
	
	/**
	 * TODO
	 * @param offset
	 * @param length
	 */
	public void requestRankings(int offset, int length) {
	}
	
	/**
	 * TODO
	 * @param answer
	 */
	public void submitAnswer(int answer, boolean correct) {
	}
	
	/**
	 * TODO
	 */
	public void leaveMatch() {
	}
}