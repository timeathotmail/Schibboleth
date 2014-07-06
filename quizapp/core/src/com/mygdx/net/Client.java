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
public class Client implements IClient {
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
	
	@Override
	public void register(String username, String password) {
		NetUtils.send(serverSocket, new AuthRequest(username, password, true));
	}
	
	@Override
	public void login(String username, String password) {
		NetUtils.send(serverSocket, new AuthRequest(username, password, false));
	}
	
	@Override
	public void logout(String username) {
		NetUtils.send(serverSocket, new LogoutRequest());
	}

	@Override
	public void changeUserData(String username, String pw1, String pw2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendChallenge(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptChallenge(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void denyChallenge(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestRankings(int offset, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitAnswer(int answer, boolean correct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveMatch() {
		// TODO Auto-generated method stub
		
	}
}