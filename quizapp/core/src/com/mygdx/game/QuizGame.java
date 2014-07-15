package com.mygdx.game;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

import common.entities.*;

import com.mygdx.net.Client;
import com.mygdx.net.NoConnectionException;

/**
 * Game class.
 * 
 * @author Tim Wiechers
 */
public class QuizGame extends Game implements IGame {
	/**
	 * Client used for server communication.
	 */
	private Client client;
	private LoginScreen loginScreen;
	
	public QuizGame() {
		Logger logger = Logger.getLogger("QuizGame");
		try {
			client = new Client(this);
		} catch (ConfigurationException e) {
			logger.log(Level.SEVERE, "Client misconfigured!", e);
		} catch (NoConnectionException e) {
			logger.log(Level.INFO, "No connection.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationAdapter#create()
	 */
	@Override
	public void create() {
		if(client == null) {
			playOffline();
		} else if (!autoLogin()) {
			loginScreen = new LoginScreen(this);
			setScreen(loginScreen);
		}
	}

	// =====================================================================
	// Game implementation
	// =====================================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#autoLogin()
	 */
	@Override
	public boolean autoLogin() {
		// TODO Auto-generated method stub
		//client.login("username", "password", 0);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String username, String password) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#logout()
	 */
	@Override
	public void logout() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#changeUserData(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void changeUserData(String username, String pw1, String pw2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#playOffline()
	 */
	@Override
	public void playOffline() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#searchMatch()
	 */
	@Override
	public void searchMatch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#cancelSearch()
	 */
	@Override
	public void cancelSearch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#displayUserList()
	 */
	@Override
	public void displayUserList() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#sendChallenge(common.entities.User)
	 */
	@Override
	public void sendChallenge(User user) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#displayChallenges()
	 */
	@Override
	public void displayChallenges() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#acceptChallenge(common.entities.User)
	 */
	@Override
	public void acceptChallenge(User user) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#denyChallenge(common.entities.User)
	 */
	@Override
	public void denyChallenge(User user) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#displayRankings(int, int)
	 */
	@Override
	public void displayRankings(int offset, int length) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#displaySettings()
	 */
	@Override
	public void displaySettings() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#displayStatistics()
	 */
	@Override
	public void displayStatistics() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#displayQuestion()
	 */
	@Override
	public void displayQuestion() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#submitAnswer(int, boolean)
	 */
	@Override
	public void submitAnswer(int answer, boolean correct) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#leaveMatch()
	 */
	@Override
	public void leaveMatch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#endMatch()
	 */
	@Override
	public void endMatch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onLogin(boolean, java.util.Collection)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onConnectionLost()
	 */
	@Override
	public void onConnectionLost() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onUserListChanged(boolean,
	 * common.entities.User)
	 */
	@Override
	public void onUserListChanged(boolean connected, User user) {
		if (connected) {
			// TODO add user to list
		} else {
			// TODO remove user from list
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onMatchStarted(common.entities.User,
	 * java.util.List)
	 */
	@Override
	public void onMatchStarted(User user, List<Integer> questionIds) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onChallengeDenied(common.entities.User)
	 */
	@Override
	public void onChallengeDenied(User user) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onChallengeReceived(common.entities.User)
	 */
	@Override
	public void onChallengeReceived(User user) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onOpponentLeft()
	 */
	@Override
	public void onOpponentLeft() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onOpponentAnswered(int)
	 */
	@Override
	public void onOpponentAnswered(int opponentAnswer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onError(java.lang.String)
	 */
	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onSearchCancelled()
	 */
	@Override
	public void onSearchCancelled() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onRankingsReceived(java.util.List)
	 */
	@Override
	public void onRankingsReceived(List<User> users) {
		// TODO Auto-generated method stub

	}
}
