package com.mygdx.net;

import java.net.Socket;

import com.mygdx.game.QuizGame;

import common.net.responses.AuthResponse;
import common.net.responses.UserListChangedResponse;

public class ClientInbox extends AbstractClientInbox {

	protected ClientInbox(QuizGame game, Socket serverSocket) {
		super(game, serverSocket);
	}

	@Override
	protected void process(AuthResponse obj) {
		game.onLogin(obj.isSuccess(), obj.getUsers());
	}

	@Override
	protected void process(UserListChangedResponse obj) {
		game.onUserListChanged(obj.hasEntered(), obj.getUser());
	}
}
