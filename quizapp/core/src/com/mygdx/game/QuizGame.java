package com.mygdx.game;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import common.entities.*;
import common.net.SocketWriteException;
import common.utils.Config;

import com.mygdx.net.Client;
import com.mygdx.net.NoConnectionException;

/**
 * Game class.
 * 
 * @author Tim Wiechers, halfelv
 */
public class QuizGame extends Game implements IGame{
	/**
	 * Client used for server communication.
	 */
	private Client client;
	private int revision;

	private LoginScreen loginScreen;
	private MainScreen mainScreen;
	private OfflineScreen offlineScreen;
	private OptionsScreen options;
	
	private OnlineScreen onlineScreen;
	
	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;
	
	
	public QuizGame() {
		Logger logger = Logger.getLogger("QuizGame");
		try {
			Config cfg = Config.get();
			revision = cfg.getInt("revision");
			client = new Client(this, cfg.get("IP"), cfg.getInt("PORT"));
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
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
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		if (client == null) {
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
		// client.login("username", "password", 0);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String username, String password) {
		if (username.isEmpty()) {
			loginScreen.showErrorMsg("enter a username");
		} else if (password.isEmpty()) {
			loginScreen.showErrorMsg("enter a password");
		} else {
			try {
				client.login(username, password, revision);
			} catch (SocketWriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void register(String username, String password, String confirmation) {

		if (username.isEmpty()) {
			loginScreen.showErrorMsg("enter a username");
		} else if (password.isEmpty()) {
			loginScreen.showErrorMsg("enter a password");
		} else if (!password.equals(confirmation)) {
			loginScreen.showErrorMsg("passwords do not match");
		} else {
			try {
				client.register(username, password, revision);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketWriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		offlineScreen = new OfflineScreen(this);
		setScreen(offlineScreen);
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
		options = new OptionsScreen(this);
		setScreen(options);

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
		if (success) {
			mainScreen = new MainScreen(this, users);
			setScreen(mainScreen);
		} else {
			loginScreen.showErrorMsg("something went wrong");
			// TODO wrong name/pw, name is taken, bad password etc.
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
