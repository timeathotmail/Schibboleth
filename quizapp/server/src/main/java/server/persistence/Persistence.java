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
	 * @throws SQLException
	 *             if any database query fails
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
	 * @throws SQLException
	 *             if any database query fails
	 * @throws IllegalArgumentException
	 *             if username and/or passwords don't meet standards
	 */
	User registerUser(String username, String password) throws SQLException,
			IllegalArgumentException;

	/**
	 * Changes the user credentials.
	 * 
	 * @param user
	 *            the user to change credentials of
	 * @param username
	 *            desired username
	 * @param password
	 *            desired password
	 * @param confirmation
	 *            password confirmation
	 * @throws SQLException
	 *             if any database query fails
	 * @throws IllegalArgumentException
	 *             if username and/or passwords don't meet standards or password
	 *             and confirmation are not equal.
	 */
	void changeUserCredentials(User user, String username, String password,
			String confirmation) throws SQLException, IllegalArgumentException;

	/**
	 * @return a list of banned expressions not to be used in usernames
	 * @throws SQLException
	 *             if any database query fails
	 */
	List<String> getBadWords() throws SQLException;

	/**
	 * Updates a user's data based on his ID.
	 * 
	 * @param user
	 *            user to update
	 * @throws SQLException
	 *             if any database query fails
	 */
	void updateUser(User user) throws SQLException;

	/**
	 * Removes a user from the database.
	 * 
	 * @param user
	 *            to remove
	 * @throws SQLException
	 *             if any database query fails
	 */
	void removeUser(User user) throws SQLException;

	/**
	 * Searches user by name.
	 * 
	 * @param username
	 *            user's name
	 * @return the user on success, null else
	 * @throws SQLException
	 *             if any database query fails
	 */
	User getUser(String username) throws SQLException;

	/**
	 * @return a list of all users in the database
	 * @throws SQLException
	 *             if any database query fails
	 */
	List<User> getUsers() throws SQLException;

	/**
	 * Adds a new question to the database.
	 * 
	 * @param question
	 *            question to add
	 * @throws SQLException
	 *             if any database query fails
	 */
	void addQuestion(Question question) throws SQLException;

	/**
	 * Updates a question's data based on its ID.
	 * 
	 * @param question
	 *            question to update
	 * @throws SQLException
	 *             if any database query fails
	 */
	void updateQuestion(Question question) throws SQLException;

	/**
	 * Remove a question from the database.
	 * 
	 * @param question
	 *            question to remove
	 * @throws SQLException
	 *             if any database query fails
	 */
	void removeQuestion(Question question) throws SQLException;

	/**
	 * @return a list of all questions in the database
	 * @throws SQLException
	 *             if any database query fails
	 */
	List<Question> getQuestions() throws SQLException;

	/**
	 * Saves the result of a match in the database.
	 * 
	 * @param match
	 *            match to save
	 * @throws SQLException
	 *             if any database query fails
	 */
	void saveMatch(Match match) throws SQLException;

	/**
	 * @param user
	 *            the user to fetch matches of
	 * @return the running matches of a user
	 * @throws SQLException
	 *             if any database query fails
	 */
	List<Match> getRunningMatches(User user) throws SQLException;

	/**
	 * @param user
	 *            the user to fetch challenges of
	 * @return the unanswered challenges of a user
	 * @throws SQLException
	 *             if any database query fails
	 */
	List<Challenge> getChallenges(User user) throws SQLException;

	/**
	 * Adds a challenge to the database.
	 * 
	 * @param challenge
	 *            the challenge to insert
	 * @throws SQLException
	 *             if any database query fails
	 */
	void saveChallenge(Challenge challenge) throws SQLException;

	/**
	 * Removes a challenge from the database.
	 * 
	 * @param challenge
	 *            the challenge to remove
	 * @throws SQLException
	 *             if any database query fails
	 */
	void removeChallenge(Challenge challenge) throws SQLException;
}
