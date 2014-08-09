package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

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
		
	/**makes Game to a singletone*/
	private static QuizGame INSTANCE;

	/*default screen resolution*/
	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;
	
	/**
	 * 
	 */

	private QuizGame() {
		Logger logger = Logger.getLogger("QuizGame");		
		try {
			Config cfg = Config.get();
			Client.getInstance(this, cfg.get("IP"), cfg.getInt("PORT"));
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @returns an instance of this Game
	 */
	public static QuizGame getInstance(){
		if(INSTANCE == null){
			INSTANCE = new QuizGame(); 
		}
		return INSTANCE;
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
		
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(ScreenSelector.GAME);		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#autoLogin()
	 * */
	 
	@Override
	public boolean autoLogin() {
		
		// TODO Auto-generated method stub
		// client.login("username", "password", 0);
		return false;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#logout()
	 */
	@Override
	public void logout() {
		try {
			Client.getInstance().logout();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}

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
	 * @see com.mygdx.game.IGame#displaySettings()
	 */
	@Override
	public void displaySettings() {
		ScreenManager.getInstance().show(ScreenSelector.OPTIONS);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onLogin(boolean, java.util.Collection)
	 */
	@Override
	public void onLogin(boolean success, int i, Collection<User> users, List<Match> list, List<Challenge> list2) {
		if (success) {
			ScreenManager.getInstance().show(ScreenSelector.MAIN_MENU);
		} else {
			ScreenManager.getInstance().show(ScreenSelector.LOGIN);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onConnectionLost()
	 */
	@Override
	public void onConnectionLost() {
		//TODO dialog with no connection
		ScreenManager.getInstance().show(ScreenSelector.LOGIN);

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
			//TODO add user
		} else {
			// TODO remove user from list
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onMatchStarted(common.entities.Match)
	 */
	@Override
	public void onMatchStarted(Match match) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onChallengeDenied(common.entities.User)
	 */
	@Override
	public void onChallengeDenied(Challenge challenge) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onChallengeReceived(common.entities.User)
	 */
	@Override
	public void onChallengeReceived(Challenge challenge) {
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
	 * @see com.mygdx.game.IGame#onOpponentAnswered(int, int, boolean)
	 */
	@Override
	public void onOpponentAnswered(int matchId, int opponentAnswer, boolean inTime) {
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
	
	public void onUserSearchResult(User user){
		//TODO
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose(){
		super.dispose();
		ScreenManager.getInstance().dispose();
	}

}
