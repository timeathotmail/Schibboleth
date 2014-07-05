package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.QuizGame;

import common.net.Communication;

public class ClientInbox implements Runnable {
	private final ClientAdapter adapter;
	private final Socket serverSocket;
	private final QuizGame game;

	public ClientInbox(Socket socket, QuizGame game) {
		this.serverSocket = socket;
		this.game = game;
		this.adapter = ClientAdapter.getInstance(game);
	}

	@Override
	public void run() {
		try {
			while (true) {
				adapter.process(Communication.read(serverSocket));
			}
		} catch (RuntimeException e) {
			game.onConnectionLost();
		}
	}
}
