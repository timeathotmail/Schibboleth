package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.QuizGame;

import common.net.NetUtils;
import common.net.responses.*;

public abstract class AbstractClientInbox implements Runnable {
	private final Socket serverSocket;
	protected final QuizGame game;
	
	protected AbstractClientInbox(QuizGame game, Socket serverSocket) {
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
	
	abstract protected void process(AuthResponse obj);
	abstract protected void process(UserListChangedResponse obj);
}