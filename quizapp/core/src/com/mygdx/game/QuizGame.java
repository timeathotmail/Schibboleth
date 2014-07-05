package com.mygdx.game;

import java.util.Collection;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import common.entities.*;
import com.mygdx.net.Client;

public class QuizGame extends ApplicationAdapter implements IGame {
	private final Client client = new Client(this);
	private SpriteBatch batch;
	private Texture img;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		client.login("username", "password");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void logout() {
	}
	
	@Override
	public void onNoConnection() {
		Gdx.app.log("Game", "no connection");
		// TODO display error
	}

	@Override
	public void onLogin(boolean success, Collection<User> users) {
		Gdx.app.log("Game", "login = " + success);
		if (success) {
			// TODO add users to list
			// TODO enter menu
		} else {
			// TODO display error
		}
	}

	@Override
	public void onConnectionLost() {
		Gdx.app.log("Game", "connection lost");
		// TODO display error
	}

	@Override
	public void onUserListChanged(boolean add, User user) {
		if(add) {
			// TODO add user to list
		} else {
			// TODO remove user from list
		}
	}

	@Override
	public void autoLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeUserData(String username, String pw1, String pw2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playOffline() {
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
	public void displayUserList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayChallenges() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void denyChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayRankings(int offset, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displaySettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayStatistics() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitAnswer(int question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMatchFound(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChallengeAccepted(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChallengeDenied(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChallengeReceived(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpponentLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpponentAnswered(int question) {
		// TODO Auto-generated method stub
		
	}
}
