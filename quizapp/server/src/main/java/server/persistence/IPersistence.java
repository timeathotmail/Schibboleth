package server.persistence;

import java.util.List;

import common.entities.*;

/**
 * Provides an interface for database access functionality.
 * 
 * @author Tim Wiechers
 */
public interface IPersistence {
	/**
	 * Searches a user with given credentials.
	 * @param username user's nickname
	 * @param password user's password
	 * @return the user on success, null else 
	 */
	User loginUser(String username, String password);
	
	/**
	 * Creates a new user and adds him to the database.
	 * @param username user's nickname
	 * @param password user's password
	 * @return the user on success, null else 
	 */
	User registerUser(String username, String password);
	
	boolean changeUserCredentials(String username, String password, String confirmation);
	
	/**
	 * Updates a user's data based on his ID.
	 * @param user user to update
	 * @return true on success
	 */
	boolean updateUser(User user);
	
	/**
	 * Removes a user from the database.
	 * @param user to remove
	 * @return true on success
	 */
	boolean removeUser(User user);
	
	/**
	 * Searches user by name.
	 * @param username user's name
	 * @return the user on success, null else
	 */
	User getUser(String username);
	
	/**
	 * @return a list of all users in the database
	 */
	List<User> getUsers();
	
	/**
	 * Returns a list of users sorted by their rank.
	 * @param offset first rank
	 * @param length amount of users to return
	 * @return a section of the ranking
	 */
	List<User> getRankedUsers(int offset, int length);
	
	/**
	 * Adds a new question to the database.
	 * @param question question to add
	 * @return true on success
	 */
	boolean addQuestion(Question question);
	
	/**
	 * Updates a question's data based on its ID.
	 * @param question question to update
	 * @return true on success
	 */
	boolean updateQuestion(Question question);
	
	/**
	 * Remove a question from the database.
	 * @param question question to remove
	 * @return true on success
	 */
	boolean removeQuestion(Question question);
	
	/**
	 * @return a list of all questions in the database
	 */
	List<Question> getQuestions();
	
	/**
	 * Saves the result of a match in the database.
	 * @param match match to save
	 * @return true on success
	 */
	boolean saveMatch(Match match);
}
