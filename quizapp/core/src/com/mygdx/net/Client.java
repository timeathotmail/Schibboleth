package com.mygdx.net;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.QuizGame;

import common.net.NetUtils;
import common.net.requests.*;

/**
 * 
 * 
 * @author Tim Wiechers
 */
public class Client {
	private Socket serverSocket;
	
	public Client(QuizGame game) {
		try {
			serverSocket = new Socket(NetUtils.IP, NetUtils.PORT);
			new Thread(new ClientInbox(game, serverSocket)).start();
		} catch (Exception e) {
			Gdx.app.error("Client", "couldn't connect to server", e);
			game.onNoConnection();
		}
	}
	
	public void register(String username, String password) {
		NetUtils.send(serverSocket, new AuthRequest(username, password, true));
	}
	
	public void login(String username, String password) {
		NetUtils.send(serverSocket, new AuthRequest(username, password, false));
	}
	
	public void logout(String username) {
		NetUtils.send(serverSocket, new LogoutRequest());
	}
}