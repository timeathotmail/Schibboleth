package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.IGame;

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
	
	/**
	 * Creates an instance.
	 * @param game client's game instance
	 * @param serverSocket server socket
	 */
	public ClientInbox(IGame game, Socket serverSocket) {
		this.game = game;
		this.serverSocket = serverSocket;
	}
	
	/**
	 * Waits for a server message, then tries to map and process it.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				String json = NetUtils.read(serverSocket);
				
				// === case 1: AuthResponse ===
				AuthResponse obj = NetUtils.fromJson(json, AuthResponse.class);
				if(obj != null) {
					game.onLogin(obj.isSuccess(), obj.getUsers());
					return;
				}
				
				// === case 2: UserListChangedResponse ===
				UserListChangedResponse obj2 = NetUtils.fromJson(json, UserListChangedResponse.class);
				if(obj2 != null) {
					game.onUserListChanged(obj2.hasConnected(), obj2.getUser());
					return;
				}
				
				//TODO: rest
			}
		} catch (RuntimeException e) {
			game.onConnectionLost();
		}
	}
}