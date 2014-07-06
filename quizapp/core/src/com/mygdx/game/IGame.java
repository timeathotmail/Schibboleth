package com.mygdx.game;

import java.util.Collection;

import common.entities.*;

/**
 * Defines functionality the game has to provide.
 * 
 * @author Tim Wiechers
 */
public interface IGame {
	/*==========================================================
	 * Login / Logout
	 *==========================================================*/
	
	/**
	 * Auto-login with saved credentials.
	 */
	void autoLogin();
	
	/**
	 * Login with provided username and password.
	 * Save credentials for auto-login.
	 * @param username username
	 * @param password password
	 */
	void login(String username, String password);
	
	/**
	 * Log out of active account.
	 * Remove credentials for auto-login
	 */
	void logout();
	
	/**
	 * Change username and/or password
	 * @param username new username
	 * @param pw1 new password
	 * @param pw2 new password confirmation
	 */
	void changeUserData(String username, String pw1, String pw2);
	
	/**
	 * Enter offline mode.
	 */
	void playOffline();
	
	/*==========================================================
	 * Menu interaction
	 *==========================================================*/
	
	/**
	 * 
	 */
	void searchMatch();
	void cancelSearch();
	void displayUserList();
	void sendChallenge(User user);
	void displayChallenges();
	void acceptChallenge(User user);
	void denyChallenge(User user);
	void displayRankings(int offset, int length);
	void displaySettings();
	void displayStatistics();
	
	/*==========================================================
	 * In-match interaction
	 *==========================================================*/
	
	void submitAnswer(int question);
	void leaveMatch();
	void endMatch();
	
	/*==========================================================
	 * Server notifications
	 *==========================================================*/
	
	void onNoConnection();
	void onLogin(boolean success, Collection<User> users);
	void onConnectionLost();
	void onUserListChanged(boolean add, User user);
	void onMatchFound(User user);
	void onChallengeAccepted(User user);
	void onChallengeDenied(User user);
	void onChallengeReceived(User user);
	void onOpponentLeft();
	void onOpponentAnswered(int question);
}
