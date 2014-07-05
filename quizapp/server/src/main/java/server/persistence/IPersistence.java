package server.persistence;

import java.util.List;

import common.entities.*;

public interface IPersistence {
	User loginUser(String username, String password);
	User registerUser(String username, String password);
	boolean updateUser(User user);
	boolean removeUser(User user);
	User getUser(String username);
	List<User> getUsers();
	List<User> getRankedUsers(int offset, int length);
	
	boolean addQuestion(Question question);
	boolean updateQuestion(Question question);
	boolean removeQuestion(Question question);
	List<Question> getQuestions();
	
	boolean saveMatch(Match match);
}
