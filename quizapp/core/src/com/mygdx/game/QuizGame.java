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
	
	/*saves users online*/
	private List<User> users;
	/*saves challenges */
	private List<Challenge> challenges;
	/*saves matches*/
	private List<Match> matches;
	
	/*default screen resolution*/
	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;
	
	/**
	 * 
	 */

	private QuizGame() {
		Logger logger = Logger.getLogger("QuizGame");
		users = new ArrayList<User>();
		challenges = new ArrayList<Challenge>();
		matches = new ArrayList<Match>();
		
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
			ScreenManager.getInstance().show(ScreenSelector.LOGIN);
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
		try {
			Client.getInstance().searchMatch();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#cancelSearch()
	 */
	@Override
	public void cancelSearch() {
		try {
			Client.getInstance().cancelSearch();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#sendChallenge(common.entities.User)
	 */
	@Override
	public void sendChallenge(User user) {		
		try {
			Client.getInstance().sendChallenge(user);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * displays a list of accepted challenges
	 * @return
	 */

	List<Challenge> displayChallenges() {
		return challenges;

	}
	/**
	 * displays a list of users online
	 * @param 
	 */
	List<User> displayUsersOnline(){
		return users;
	}
	/**
	 * 
	 * @return a list of matches
	 */
	List<Match> displayMatches(){
		return matches;
	}
	
	/**
	 * returns a match by id 
	 */
	Match getMatch(int id){
		if(id < 0){
			throw new IllegalArgumentException("Parameter is null");
		}
		int i =0;
		while(i < matches.size()){
			if(matches.get(i).getRowId() == id){
				return matches.get(i);
			}
			i++;
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#acceptChallenge(common.entities.User)
	 */
	@Override
	public void acceptChallenge(Challenge challenge) {
		try {
			Client.getInstance().acceptChallenge(challenge);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#denyChallenge(common.entities.User)
	 */
	@Override
	public void denyChallenge(Challenge challenge) {
		try {
			Client.getInstance().denyChallenge(challenge);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}

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
	 * i Zeitlimit
	 */
	@Override
	public void onLogin(boolean success, int i, List<User> users, List<Match> list, List<Challenge> list2) {
		if (success) {
			this.users = users;
			this.challenges = list2;
			this.matches = list;
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
			users.add(user);			
		} else {
			users.remove(user);			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onMatchStarted(common.entities.Match)
	 */
	@Override
	public void onMatchStarted(Match match) {
		matches.add(match);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onChallengeDenied(common.entities.User)
	 */
	@Override
	public void onChallengeDenied(Challenge challenge) {
		int i = 0;
		while(i < challenges.size()){
			if(challenges.get(i) == challenge){
				challenges.remove(challenge);
			}
			i++;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onChallengeReceived(common.entities.User)
	 */
	@Override
	public void onChallengeReceived(Challenge challenge) {
		challenges.add(challenge);

	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.IGame#onOpponentAnswered(int, int, boolean)
	 */
	@Override
	public void onOpponentAnswered(int matchId, int opponentAnswer, boolean inTime) {
		try {
			Client.getInstance().submitAnswer(matchId, opponentAnswer, inTime);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}

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
		ScreenManager.getInstance().show(ScreenSelector.MAIN_MENU);

	}
	
	public void onUserSearchResult(User user){
		try {
			Client.getInstance().searchUser(user.getName());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketWriteException e) {
			e.printStackTrace();
		}
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
