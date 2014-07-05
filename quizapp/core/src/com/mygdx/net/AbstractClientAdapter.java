package com.mygdx.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.game.QuizGame;
import common.net.responses.*;

public abstract class AbstractClientAdapter {
	private final ObjectMapper mapper = new ObjectMapper();
	protected final QuizGame game;
	
	protected AbstractClientAdapter(QuizGame game) {
		this.game = game;
	}
	
	private <T> T getObj(String json, Class<T> classOf) {
		try {
			return mapper.readValue(json, classOf);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void process(String json) {
		AuthResponse obj = getObj(json, AuthResponse.class);
		if(obj != null) {
			process(obj);
		}
		
		UserListChangedResponse obj2 = getObj(json, UserListChangedResponse.class);
		if(obj2 != null) {
			process(obj2);
		}
	}
	
	abstract protected void process(AuthResponse obj);
	abstract protected void process(UserListChangedResponse obj);
}