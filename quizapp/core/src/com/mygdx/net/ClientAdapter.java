package com.mygdx.net;

import com.mygdx.game.QuizGame;
import common.net.responses.*;

public class ClientAdapter extends AbstractClientAdapter {
	private static ClientAdapter instance;
	
	public static ClientAdapter getInstance(QuizGame game) {
		if(instance == null) {
			instance = new ClientAdapter(game);
		}
		return instance;
	}
	
	private ClientAdapter(QuizGame game) {
		super(game);
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
