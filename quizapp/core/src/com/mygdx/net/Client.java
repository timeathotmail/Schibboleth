package com.mygdx.net;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.QuizGame;

import common.net.Communication;
import common.net.requests.*;

public class Client {
	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 11111;
	
	private Socket serverSocket;
	
	public Client(QuizGame game) {
		try {
			serverSocket = new Socket(SERVER_IP, SERVER_PORT);
			new Thread(new ClientInbox(serverSocket, game)).start();
		} catch (Exception e) {
			Gdx.app.error("Client", "couldn't connect to server", e);
			game.onNoConnection();
		}
	}
	
	public void register(String username, String password) {
		Communication.send(serverSocket, new AuthRequest(username, password, true));
	}
	
	public void login(String username, String password) {
		Communication.send(serverSocket, new AuthRequest(username, password, false));
	}
	
	public void logout(String username) {
		Communication.send(serverSocket, new LogoutRequest());
	}
}