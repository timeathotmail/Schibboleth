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
	void autoLogin();
	
	/**
	 * Login with provided username and password.
	 * Save credentials for auto-login.
	 * Enter main menu.
	 * @param username username
	 * @param password password
	 */
	void login(String username, String password);
	
	/**
	 * Log out of active account.
	 * Remove credentials for auto-login.
	 * Enter main menu.
	 */
	void logout();
	
	/**
	 * Change username and/or password.
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
	 * Display the user list.
	 */
	void displayUserList();
	
	/**
	 * Send a challenge to another user.
	 * @param user challenged user
	 */
	void sendChallenge(User user);
	
	/**
	 * Display received challenges.
	 */
	void displayChallenges();
	
	/**
	 * Accept a challenge of another user.
	 * @param user challenger
	 */
	void acceptChallenge(User user);
	
	/**
	 * Deny and remove a received challenge.
	 * @param user challenger
	 */
	void denyChallenge(User user);
	
	/**
	 * Display the rankings.
	 * @param offset offset used for displaying pages
	 * @param length amount of players to show
	 */
	void displayRankings(int offset, int length);
	
	/**
	 * Display the settings.
	 */
	void displaySettings();
	
	/**
	 * Display the active user's stats.
	 */
	void displayStatistics();
	
	/*==========================================================
	 * In-match interaction
	 *==========================================================*/
	
	/**
	 * Display question and answers.
	 */
	void displayQuestion();
	
	/**
	 * Choosing an answer for a question in-game. 
	 * Send the choice to the server.
	 * Highlight the chosen answer.
	 * @param answer index of the answer
	 */
	void submitAnswer(int answer, boolean correct);
	
	/**
	 * Leave the current match.
	 * Inform the server.
	 * Enter the main menu.
	 */
	void leaveMatch();
	
	/**
	 * Confirm the result and enter the main menu.
	 */
	void endMatch();
	
	/*==========================================================
	 * Server notifications
	 *==========================================================*/
	
	/**
	 * Called if login not avaiable. 
	 * Display error message.
	 */
	void onNoConnection();
	
	/**
	 * Called on login response. 
	 * Enter main menu on success or display error message.
	 * @param success login was successful
	 * @param users the active user list
	 */
	void onLogin(boolean success, Collection<User> users);
	
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
	
	/**
	 * Called if opponent found.
	 * Display match start screen.
	 * @param user found opponent
	 * @param questionIds list of the questions' ids
	 */
	void onMatchStarted(User user, List<Integer> questionIds);
	
	/**
	 * Called on denied challenge.
	 * Notify the user that the challenge was denied.
	 * @param user denying user
	 */
	void onChallengeDenied(User user);
	
	/**
	 * Called on received challenge.
	 * Notify the user that a challenge was received.
	 * @param user
	 */
	void onChallengeReceived(User user);
	
	/**
	 * Called if the opponent left the game.
	 * End match.
	 */
	void onOpponentLeft();
	
	/**
	 * Called when opponent answered or the countdown expired.
	 * Display correct answer and the one the opponent selected.
	 * @param opponentAnswer opponent's answer
	 */
	void onOpponentAnswered(int opponentAnswer);
	
	/**
	 * Called when a request couldn't be processed on the server.
	 * @param message the error message
	 */
	void onError(String message);
	
	/**
	 * Called when the search for match was cancelled before a match could be found.
	 */
	void onSearchCancelled();
	
	/**
	 * Called when the server sends a section of the leaderboard.
	 * @param users the ranked users
	 */
	void onRankingsReceived(List<User> users);
}
