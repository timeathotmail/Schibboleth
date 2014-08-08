package server.persistence;

import java.sql.SQLException;
import java.util.List;

import common.entities.*;

/**
 * Provides an interface for database access functionality.
 * 
 * @author Tim Wiechers
 */
public interface Persistence {

	/**
	 * Searches a user with given credentials.
	 * 
	 * @param username
	 *            user's nickname
	 * @param password
	 *            user's password
	 * @return the user on success, null else
	 */
	User loginUser(String username, String password) throws SQLException;

	/**
	 * Creates a new user and adds him to the database.
	 * 
	 * @param username
	 *            user's nickname
	 * @param password
	 *            user's password
	 * @return the user on success, null else
	 */
	User registerUser(String username, String password) throws SQLException,
			IllegalArgumentException;

	void changeUserCredentials(User user, String username, String password,
			String confirmation) throws SQLException, IllegalArgumentException;

	/**
	 * Updates a user's data based on his ID.
	 * 
	 * @param user
	 *            user to update
	 * @return true on success
	 */
	void updateUser(User user) throws SQLException;

	/**
	 * Removes a user from the database.
	 * 
	 * @param user
	 *            to remove
	 * @return true on success
	 */
	void removeUser(User user) throws SQLException;

	/**
	 * Searches user by name.
	 * 
	 * @param username
	 *            user's name
	 * @return the user on success, null else
	 */
	User getUser(String username) throws SQLException;

	/**
	 * @return a list of all users in the database
	 */
	List<User> getUsers() throws SQLException;

	/**
	 * Adds a new question to the database.
	 * 
	 * @param question
	 *            question to add
	 * @return true on success
	 */
	void addQuestion(Question question) throws SQLException;

	/**
	 * Updates a question's data based on its ID.
	 * 
	 * @param question
	 *            question to update
	 * @return true on success
	 */
	void updateQuestion(Question question) throws SQLException;

	/**
	 * Remove a question from the database.
	 * 
	 * @param question
	 *            question to remove
	 * @return true on success
	 */
	void removeQuestion(Question question) throws SQLException;

	/**
	 * @return a list of all questions in the database
	 */
	List<Question> getQuestions() throws SQLException;

	List<Question> getQuestions(int revision) throws SQLException;

	/**
	 * Saves the result of a match in the database.
	 * 
	 * @param match
	 *            match to save
	 * @return true on success
	 */
	void saveMatch(Match match) throws SQLException;
	
	List<Match> getRunningMatches(User user) throws SQLException;
	
	List<String> getBadWords() throws SQLException;
	
	List<Challenge> getChallenges(User user) throws SQLException;
	void saveChallenge(Challenge challenge) throws SQLException;
	void removeChallenge(Challenge challenge) throws SQLException;
}
