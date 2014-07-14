package servertest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.entities.Match;
import common.entities.User;
import server.persistence.Data;

@FixMethodOrder(MethodSorters.JVM)
public class DataTest {
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

	@AfterClass
	public static void testRemoveUser() throws SQLException {
		for (User user : users) {
			persistence.removeUser(user);
		}

		assertTrue(persistence.getUsers().size() == 0);
	}

	@Test
	public void testGetUser() throws SQLException {
		for (User user : users) {
			assertEquals(user, persistence.getUser(user.getName()));
		}
	}

	@Test
	public void testLoginUser() throws SQLException {
		for (User user : users) {
			assertEquals(user, persistence.loginUser(user.getName(), PW));
		}
	}

	@Test
	public void testUpdateUser() throws IllegalArgumentException,
			SQLException {
		for (User user : users) {
			String newName = user.getName() + "_";
			persistence.changeUserCredentials(user, newName, "_", "_");
			assertEquals(user.getName(), newName);
		}

		for (User user : users) {
			assertEquals(user, persistence.loginUser(user.getName(), "_"));
		}

		for (User user : users) {
			persistence.changeUserCredentials(user,
					user.getName().substring(0, user.getName().length() - 1),
					PW, PW);
		}
	}

	@Test
	public void testGetUsers() throws SQLException {
		assertEquals(Arrays.asList(users), persistence.getUsers());
	}

	@Test
	public void testMatchesAndRankings() throws SQLException {
		persistence.saveMatch(new Match(users[0], users[1], 5, 6));
		persistence.saveMatch(new Match(users[1], users[0], 10, 2));
		persistence.saveMatch(new Match(users[1], users[2], 1, 0));
		persistence.saveMatch(new Match(users[3], users[2], 7, 3));
		persistence.saveMatch(new Match(users[2], users[3], 5, 2));

		for (int i = 0; i < users.length; i++) {
			users[i] = persistence.getUser(users[i].getName());
		}

		assertTrue(users[0].getPointCount() == 7 && users[0].getWinCount() == 0
				&& users[0].getMatchCount() == 2);
		assertTrue(users[1].getPointCount() == 17
				&& users[1].getWinCount() == 3 && users[1].getMatchCount() == 3);
		assertTrue(users[2].getPointCount() == 8 && users[2].getWinCount() == 1
				&& users[2].getMatchCount() == 3);
		assertTrue(users[3].getPointCount() == 9 && users[3].getWinCount() == 1
				&& users[3].getMatchCount() == 2);

		List<User> ranking = persistence.getRankedUsers(0, 27);
		assertEquals(users[1], ranking.get(0));
		assertEquals(users[0], ranking.get(3));
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
	public void testGetQuestionsByRevision() {
		fail("Not yet implemented");
	}
}
