package com.mygdx.net;

/**
 * Interface for sending requests to the server.
 * 
 * @author Tim Wiechers
 */
public interface IClient {
	/**
	 * Requests a user registration.
	 * @param username desired username
	 * @param password desired password
	 */
	void register(String username, String password);
	
	/**
	 * Requests a user login.
	 * @param username username
	 * @param password password
	 */
	void login(String username, String password);
	
	/**
	 * Informs the server of a logout
	 * @param username username
	 */
	void logout(String username);
	
	/**
	 * Requests a user data change.
	 * @param username new username
	 * @param pw1 new password
	 * @param pw2 new password confirmation
	 */
	void changeUserData(String username, String pw1, String pw2);
	
	/**
	 * Requests the server to search for a match.
	 */
	void searchMatch();
	
	/**
	 * Requests the server to cancel the search for a match.
	 */
	void cancelSearch();
	
	/**
	 * Requests the server to send another user a challenge.
	 * @param username receiving user
	 */
	void sendChallenge(String username);
	
	/**
	 * Requests the server to start a match for the receiving user.
	 * @param username receiving user
	 */
	void acceptChallenge(String username);
	
	/**
	 * Requests the server to notify another user about a denied challenge.
	 * @param username denied user
	 */
	void denyChallenge(String username);
	
	/**
	 * Requests the server to send rankings to the client.
	 * @param offset first rank
	 * @param length amount of users
	 */
	void requestRankings(int offset, int length);
	
	/**
	 * Requests the server to send the client's answer choice to his opponent.
	 * @param answer answer's index
	 */
	void submitAnswer(int answer, boolean correct);
	
	/**
	 * Requests the server to notify the client's opponent that the client left the game.
	 */
	void leaveMatch();
}