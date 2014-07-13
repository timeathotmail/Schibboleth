package servertest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import common.entities.User;
import server.persistence.Data;

public class DataTest {
	private static final Logger logger = Logger.getLogger("DataTest");
	private static final String PW = "passwort";
	private static final User[] users = new User[] { new User("EINS", false),
			new User("ZWEI", false), new User("DREI", false),
			new User("VIER", false) };

	private static Data persistence;

	@BeforeClass
	public static void testRegisterUser() throws Exception {
		persistence = Data.getInstance();

		for (User user : users) {
			User r_user = persistence.registerUser(user.getName(), PW);

			assertEquals(user.getName(), r_user.getName());
			assertEquals(r_user.getMatchCount(), 0);
			assertEquals(r_user.getPointCount(), 0);
			assertEquals(r_user.getWinCount(), 0);
			assertTrue(r_user.getId() > 0);

			user.setId(r_user.getId());
		}
	}

	@Test
	public void testLoginUser() {
		try {
			for (User user : users) {
				assertEquals(user, persistence.loginUser(user.getName(), PW));
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Test failed", e);
			fail();
		}
	}

	@Test
	public void testChangeUserCredentials() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRankedUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddQuestion() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateQuestion() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveQuestion() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetQuestions() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetQuestionsInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveMatch() {
		fail("Not yet implemented");
	}

}
