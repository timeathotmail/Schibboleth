/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import server.persistence.Data;
import common.entities.Match;
import common.entities.Question;
import common.entities.User;
import common.entities.Question.Category;
import common.entities.User.Role;

/**
 * DieKommentare aus dem Interface IPersistance wuerden fuer Bequemlichkeit kopiert.
 * Sie sind spaeter zu entfernen
 * @author halfelv
 *
 */
public class DataTest {

	/**
	 * Test method for {@link server.persistence.Data#loginUser(java.lang.String, java.lang.String)}.
	 */
	/**
	 * Searches a user with given credentials.
	 * @param username user's nickname
	 * @param password user's password
	 * @return the user on success, null else 
	 */
	@Test
	public void testLoginUser() {
		User user = new User("Bob", Role.Player);
		
		
		User user1 = new User("Bib", Role.Admin);
		
	}

	/**
	 * Test method for {@link server.persistence.Data#registerUser(java.lang.String, java.lang.String)}.
	 */
	/**
	 * Creates a new user and adds him to the database.
	 * @param username user's nickname
	 * @param password user's password
	 * @return the user on success, null else 
	 */
	@Test
	public void testRegisterUser() {
		boolean exceptionThrown = false;
		//passwort null
		try{
			Data.getInstance().registerUser("Bob", null);			
		}catch(Exception e){
			exceptionThrown = true;
			e.printStackTrace();
		}
		assertEquals(true, exceptionThrown);
		
		//passwort empty
		exceptionThrown = false;
		try{
			Data.getInstance().registerUser("Bob", "");
		}catch(Exception e){
			exceptionThrown = true;
			e.printStackTrace();
		}
		assertEquals(true, exceptionThrown);
		
		//username null
		exceptionThrown = false;
		try{
			Data.getInstance().registerUser(null, "1234");
		}catch(Exception e){
			exceptionThrown = true;
			e.printStackTrace();
		}
		assertEquals(true, exceptionThrown);
		
		//username empty
				exceptionThrown = false;
				try{
					Data.getInstance().registerUser("", "1234");
				}catch(Exception e){
					exceptionThrown = true;
					e.printStackTrace();
				}
				assertEquals(true, exceptionThrown);
		
		//both null
		exceptionThrown = false;
		try{
			Data.getInstance().registerUser(null, null);			
		}catch(Exception e){
			exceptionThrown = true;
			e.printStackTrace();
		}
		assertEquals(true, exceptionThrown);
		
		//both empty
		exceptionThrown = false;
		try{
			Data.getInstance().registerUser("", "");			
		}catch(Exception e){
			exceptionThrown = true;
			e.printStackTrace();
		}
		assertEquals(true, exceptionThrown);

		//default		
		try{
			Data.getInstance().registerUser("Bob", "1234");
			User user = Data.getInstance().getUser("Bob");
			assertSame(user, Data.getInstance().getUser("Bob"));
		}catch(Exception e){
			e.printStackTrace();
		}
				
		//the same user
		
		exceptionThrown = false;
		try{
			Data.getInstance().registerUser("Bob", "12345");			
		}catch(Exception e){
			exceptionThrown = true;
			e.printStackTrace();
		}
		assertEquals(true, exceptionThrown);
	}

	/**
	 * Test method for {@link server.persistence.Data#updateUser(common.entities.User)}.
	 */
	/**
	 * Updates a user's data based on his ID.
	 * @param user user to update
	 * @return true on success
	 */
	@Test
	public void testUpdateUser() {
		User user = new User("Bob", Role.Player);
		//null
		try{
			assertEquals(false, Data.getInstance().updateUser(null));
		}catch(Exception e){
			e.printStackTrace();
		}
		//default
		try{
			assertEquals(true, Data.getInstance().updateUser(user));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link server.persistence.Data#removeUser(common.entities.User)}.
	 */
	/**
	 * Removes a user from the database.
	 * @param user to remove
	 * @return true on success
	 */
	@Test
	public void testRemoveUser() {
		User user = new User("Bob", Role.Player);
		//no such user
		try{						
			assertEquals(false, Data.getInstance().removeUser(user));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//parameter null
		try{
			assertEquals(false, Data.getInstance().removeUser(null));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//default
		try{
			Data.getInstance().registerUser("Bob", "passwort");
			User user1 = Data.getInstance().getUser("Bob");
			Data.getInstance().removeUser(user1);
			assertNull(Data.getInstance().getUser("Bob"));
		}catch(Exception e){
			e.printStackTrace();
		}
		//remove the same user 
		try{
			assertEquals(false, Data.getInstance().removeUser(user));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link server.persistence.Data#getUser(java.lang.String)}.
	 */
	/**
	 * Searches user by name.
	 * @param username user's name
	 * @return the user on success, null else
	 */
	@Test
	public void testGetUser() {
		User user = new User("Bob", Role.Player);
		boolean exceptionThrown = false;
		
		//parameter null
		try{
			Data.getInstance().getUser("Bob");
		}catch(Exception e){
			exceptionThrown = true;
		}
		assertEquals(true, exceptionThrown);
		
		//default
		try{
			Data.getInstance().registerUser("Bob", "passwort");
			Data.getInstance().registerUser("Bill", "123");
			User bob = Data.getInstance().getUser("Bob");
			assertEquals(user, bob);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link server.persistence.Data#getUsers()}.
	 */
	/**
	 * @return a list of all users in the database
	 */
	@Test
	public void testGetUsers() {
		try{
			Data.getInstance().registerUser("Bob", "qwew");
			Data.getInstance().registerUser("Bill", "123");
			Data.getInstance().registerUser("Dane", "passw");
			List list = Data.getInstance().getUsers();
			assertNotNull(list);
			User user2 = (User) list.get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * Test method for {@link server.persistence.Data#getRankedUsers(int, int)}.
	 */
	/**
	 * Returns a list of users sorted by their rank.
	 * @param offset first rank
	 * @param length amount of users to return
	 * @return a section of the ranking
	 */
	@Test
	public void testGetRankedUsers() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link server.persistence.Data#addQuestion(common.entities.Question)}.
	 */
	/**
	 * Throws {@link IllegalArgumentException} if parameter null 
	 * Adds a new question to the database.
	 * @param question question to add
	 * @return true on success
	 */
	@Test
	public void testAddQuestion() {
		boolean exceptionThrown = false;
		//parameter null
		try{
			Question question = new Question("", "", "", "", "", 0, Category.science, 0);
			Data.getInstance().addQuestion(question);
			Question question1 = new Question(null, null, null, null, null, 0, Category.science, 0);
			Data.getInstance().addQuestion(question1);
		}catch(Exception e){
			exceptionThrown = true;
		}
		assertEquals(true, exceptionThrown);
		//default
		try{
			Question question = new Question("To be", "or", "not", "to", "be", 0, Category.science, 0);
			assertEquals(true, Data.getInstance().addQuestion(question));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link server.persistence.Data#updateQuestion(common.entities.Question)}.
	 */
	/**
	 * 
	 * Updates a question's data based on its ID.
	 * @param question question to update. Throws {@link IllegalArgumentException} if null
	 * @return true on success
	 */
	@Test
	public void testUpdateQuestion() {
		try{
			Data.getInstance().addQuestion(null);
			
			Question question = new Question("To be", "or", "not", "to", "be", 0, Category.science, 0);
			assertEquals(true, Data.getInstance().updateQuestion(question));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link server.persistence.Data#removeQuestion(common.entities.Question)}.
	 */
	/**
	 * Remove a question from the database.
	 * @param question question to remove
	 * @return true on success
	 */
	@Test
	public void testRemoveQuestion() {
		boolean exceptionThrown = false;
		Question question = new Question("To be", "or", "not", "to", "be", 0, Category.science, 0);
		
		try{
			Data.getInstance().removeQuestion(question);
			Data.getInstance().removeQuestion(null);
		}catch(Exception e){
			exceptionThrown = true;
		}
		assertEquals(true, exceptionThrown);
		
		try{
			Data.getInstance().addQuestion(question);
			assertEquals(true, Data.getInstance().removeQuestion(question));
			assertEquals(false, Data.getInstance().removeQuestion(question));
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	/**
	 * Test method for {@link server.persistence.Data#getQuestions()}.
	 */
	/**
	 * @return a list of all questions in the database
	 */
	@Test
	public void testGetQuestions() {
		
		try{
			Question question = new Question("To be", "or", "not", "to", "be", 0, Category.science, 0);
			Question question1 = new Question("1To be", "or", "not", "to", "be", 0, Category.science, 0);
			Question question2 = new Question("2To be", "or", "not", "to", "be", 0, Category.science, 0);
			
			Data.getInstance().addQuestion(question);
			Data.getInstance().addQuestion(question1);
			Data.getInstance().addQuestion(question2);
			List list = Data.getInstance().getQuestions();
			assertNotNull(list);
			Question question0 = (Question) list.get(0);			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * Test method for {@link server.persistence.Data#saveMatch(common.entities.Match)}.
	 */
	/**
	 * Saves the result of a match in the database.
	 * @param match match to save
	 * @return true on success
	 */
	@Test
	public void testSaveMatch() {
		try{
			User user1 = new User("Bob", Role.Player);
			User user2 = new User("Kate", Role.Player);
			Match match = new Match(user1, user2, 0, 2);
			Data.getInstance().saveMatch(match);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
