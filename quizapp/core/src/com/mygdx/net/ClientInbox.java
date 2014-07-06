package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.QuizGame;

import common.net.NetUtils;
import common.net.responses.*;

public class ClientInbox implements Runnable {
	private final Socket serverSocket;
	private final QuizGame game;
	
	public ClientInbox(QuizGame game, Socket serverSocket) {
		this.game = game;
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				String json = NetUtils.read(serverSocket);
				
				AuthResponse obj = NetUtils.fromJson(json, AuthResponse.class);
				if(obj != null) {
					process(obj);
				}
				
				UserListChangedResponse obj2 = NetUtils.fromJson(json, UserListChangedResponse.class);
				if(obj2 != null) {
					process(obj2);
				}
				
				//TODO: rest
			}
		} catch (RuntimeException e) {
			game.onConnectionLost();
		}
	}
	
	private void process(AuthResponse obj) {
		game.onLogin(obj.isSuccess(), obj.getUsers());
	}

	private void process(UserListChangedResponse obj) {
		game.onUserListChanged(obj.hasConnected(), obj.getUser());
	}
}