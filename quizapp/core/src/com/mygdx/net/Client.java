package com.mygdx.net;

import com.mygdx.game.QuizGame;

import common.net.NetUtils;
import common.net.requests.*;

public class Client extends AbstractClient {
	
	public Client(QuizGame game) {
		super(game);
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