package server.persistence;

import java.util.List;

import common.entities.*;

/**
 * Provides database access.
 * 
 * @author Tim Wiechers
 */
public class Data implements IPersistence {
	/**
	 * Singleton instance
	 */
	private static Data instance;
	
	/**
	 * Initiiate instance when class loaded.
	 */
	static {
		instance = new Data();
	}
	
	/**
	 * Private constructor.
	 */
	private Data() {
	}
	
	/**
	 * @return the singleton instance
	 */
	public static Data getInstance() {
		return instance;
	}

	public User loginUser(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Olga:
	//hier soll eine Exception geworfen werden, falls einer der beiden Parameter 
	//inkorrekt ist, zB null, leerer String oder zu kurz/zu lang
	//Ich teste erstmal auf null und leerer String.
	//Dazu kommt dass ein Name nicht mehr als einmal benutzt werden darf. Das teste ich auch.
	public User registerUser(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param if null throws {@link IllegalArgumentException}
	 */
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getRankedUsers(int offset, int length) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addQuestion(Question question) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateQuestion(Question question) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeQuestion(Question question) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Question> getQuestions() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean saveMatch(Match match) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeUserCredentials(String username, String password,
			String confirmation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Question[] getQuestionsForGame(int[] ids) {
		// TODO Auto-generated method stub
		return null;
	}
}
