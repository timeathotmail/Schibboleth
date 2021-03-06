package com.mygdx.game;

import java.util.Collection;
import java.util.List;

import common.entities.*;

/**
 * Defines functionality the game has to provide.
 * 
 * @author Tim Wiechers
 */
public interface IGame {
	/*==========================================================
	 * Login / Logout / Account
	 *==========================================================*/
	
	/**
	 * Auto-login with saved credentials. 
	 * Enter main menu.
	 */
	boolean autoLogin();
	
	
	/**
	 * Log out of active account.
	 * Remove credentials for auto-login.
	 * Enter main menu.
	 */
	public void logout();

	/*==========================================================
	 * Menu interaction
	 *==========================================================*/
	
	/**
	 * Let the server search for a match.
	 * Display an info-screen.
	 */
	void searchMatch();
	
	/**
	 * Cancel the search for a match.
	 * Enter main menu.
	 */
	void cancelSearch();	

	
	/**
	 * Send a challenge to another user.
	 * @param user challenged user
	 */
	void sendChallenge(User user);	
	
	
	/**
	 * Accept a challenge of another user.
	 * @param user challenger
	 */
	void acceptChallenge(Challenge challenge);
	
	/**
	 * Deny and remove a received challenge.
	 * @param user challenger
	 */
	void denyChallenge(Challenge challenge);
	
	/**
	 * Display the settings.
	 */
	void displaySettings();
	

	
	/*==========================================================
	 * In-match interaction
	 *==========================================================*/
	
	
	
	/*==========================================================
	 * Server notifications
	 *==========================================================*/
	
	/**
	 * Called on login response. 
	 * Enter main menu on success or display error message.
	 * @param success login was successful
	 * @param i	
	 * @param users the active user list
	 * @param list
	 * @param list2
	 */
	void onLogin(boolean success, int i, List<User> users,
			List<Match> list, List<Challenge> list2);
	
	/**
	 * Called on server connection loss. 
	 * Display error message
	 */
	void onConnectionLost();
	
	/**
	 * Called on user list change.
	 * Display new user in the list or remove one.
	 * @param connected true if user connected, false if user left
	 * @param user the user
	 */
	void onUserListChanged(boolean connected, User user);
	
	void onMatchStarted(Match match);
	
	/**
	 * Called on denied challenge.
	 * Notify the user that the challenge was denied.
	 * @param user denying user
	 */
	void onChallengeDenied(Challenge challenge);
	
	/**
	 * Called on received challenge.
	 * Notify the user that a challenge was received.
	 * @param challenge
	 */
	void onChallengeReceived(Challenge challenge);

	
	/**
	 * Called when opponent answered or the countdown expired.
	 * Display correct answer and the one the opponent selected.
	 * @param opponentAnswer opponent's answer
	 */
	void onOpponentAnswered(int matchId, int opponentAnswer, boolean inTime);
	
	/**
	 * Called when a request couldn't be processed on the server.
	 * @param message the error message
	 */
	void onError(String message);
	
	/**
	 * Called when the search for match was cancelled before a match could be found.
	 */
	void onSearchCancelled();


	


	
}
