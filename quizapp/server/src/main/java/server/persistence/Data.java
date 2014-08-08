package server.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import server.persistence.constraints.*;
import server.persistence.query.InsertQueryBuilder;
import server.persistence.query.QueryBuilder;
import server.persistence.query.UpdateQueryBuilder;
import common.entities.*;
import common.entities.annotations.*;
import common.utils.Config;

/**
 * Provides database access.
 * 
 * @author Tim Wiechers
 */
public class Data implements Persistence {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Data");
	/**
	 * 
	 */
	private static Config cfg;
	/**
	 * JDBC driver class.
	 */
	private static String JDBC_DRIVER;
	/**
	 * Database location.
	 */
	private static String DB_URL;
	/**
	 * Database name.
	 */
	private static String DB_NAME;
	/**
	 * MySQL user.
	 */
	private static String MYSQL_USER;
	/**
	 * MySQL password.
	 */
	private static String MYSQL_PASS;
	/**
	 * Singleton instance
	 */
	private static Data instance;
	/**
	 * 
	 */
	private final MysqlDataSource dataSource = new MysqlDataSource();
	/**
	 * 
	 */
	private final QueryRunner run;

	// =====================================================================
	// Init database connection
	// =====================================================================

	/**
	 * @return the singleton instance
	 * @throws SQLException
	 */
	public static Data getInstance() throws SQLException {
		if (instance == null) {

			try {
				cfg = Config.get();
				JDBC_DRIVER = cfg.get("JDBC_DRIVER");
				DB_URL = cfg.get("DB_URL");
				DB_NAME = cfg.get("DB_NAME");
				MYSQL_USER = cfg.get("MYSQL_USER");
				MYSQL_PASS = cfg.get("MYSQL_PASS");
			} catch (ConfigurationException e) {
				throw new SQLException("Server is misconfigured!", e);
			}

			instance = new Data();
		}

		return instance;
	}

	/**
	 * Private constructor.
	 * 
	 * @throws SQLException
	 */
	private Data() throws SQLException {

		try {
			Class.forName(JDBC_DRIVER);
			dataSource.setUser(MYSQL_USER);
			dataSource.setPassword(MYSQL_PASS);
			dataSource.setUrl(DB_URL);
			run = new QueryRunner(dataSource);
			checkDatabaseStructure();

		} catch (ClassNotFoundException e) {
			throw new SQLException("Couldn't register JDBC driver!", e);
		}
	}

	/**
	 * Asserts that the needed database and tables exist.
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void checkDatabaseStructure() throws SQLException {
		logger.info("checking database structure...");

		run.update("DROP DATABASE " + DB_NAME);
		run.update("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
		dataSource.setUrl(DB_URL + DB_NAME);

		run.update(String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "id   INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "isAdmin BOOLEAN  NOT NULL," // FIXME immer false
				+ "password VARCHAR (30) NOT NULL, "
				+ "name VARCHAR(30)  NOT NULL UNIQUE)", getTable(User.class)));

		run.update(String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "id           INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "category     VARCHAR(50)  NOT NULL,"
				+ "text         VARCHAR(200) NOT NULL,"
				+ "answer1      VARCHAR(30)  NOT NULL,"
				+ "answer2      VARCHAR(30)  NOT NULL,"
				+ "answer3      VARCHAR(30)  NOT NULL,"
				+ "answer4      VARCHAR(30)  NOT NULL,"
				+ "correctIndex TINYINT UNSIGNED,"
				+ "revision     SMALLINT UNSIGNED NOT NULL)",
				getTable(Question.class)));

		run.update(String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "user1   INT UNSIGNED," + "user2   INT UNSIGNED,"
				+ "FOREIGN KEY(user1) REFERENCES %s(id) "
				+ "ON DELETE SET NULL,"
				+ "FOREIGN KEY(user2) REFERENCES %s(id) "
				+ "ON DELETE SET NULL)", getTable(Match.class),
				getTable(User.class), getTable(User.class)));

		run.update(String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "matchId INT UNSIGNED ,"
				+ "FOREIGN KEY(matchId) REFERENCES %s(id) "
				+ "ON DELETE SET NULL" + ")", getTable(Round.class),
				getTable(Match.class)));

		run.update(String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "roundId INT UNSIGNED ,"
				+ "questionId INT UNSIGNED ,"
				+ "answerIndex1 TINYINT UNSIGNED, "
				+ "answerIndex2 TINYINT UNSIGNED, " // TODO check if 1-4
				+ "FOREIGN KEY(roundId) REFERENCES %s(id) "
				+ "ON DELETE SET NULL, "
				+ "FOREIGN KEY(questionId) REFERENCES %s(id) "
				+ "ON DELETE SET NULL)", getTable(Answer.class),
				getTable(Round.class), getTable(Question.class)));

		run.update(String.format(
				"CREATE TABLE IF NOT EXISTS %s("
						+ "id     INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
						+ "fromId INT UNSIGNED, " + "toId   INT UNSIGNED, "
						+ "FOREIGN KEY(fromId) REFERENCES %s(id) "
						+ "ON DELETE SET NULL, "
						+ "FOREIGN KEY(toId) REFERENCES %s(id) "
						+ "ON DELETE SET NULL", getTable(Challenge.class),
				getTable(User.class), getTable(User.class)));

		run.update(String
				.format("CREATE TABLE IF NOT EXISTS BAD_WORDS(word VARCHAR(100))"));

		/*
		 * TODO in sqlite run.update("DROP TRIGGER IF EXISTS matchval");
		 * run.update(String.format("CREATE TRIGGER matchval " +
		 * "BEFORE INSERT ON %s FOR EACH ROW BEGIN " +
		 * "IF NEW.user1 = NEW.user2 " + "THEN SIGNAL SQLSTATE '45000' " +
		 * "SET MESSAGE_TEXT = 'Cannot add or " +
		 * "update match: same users'; END IF; END;", getTable(Match.class)));
		 * 
		 * run.update("DROP TRIGGER IF EXISTS matchcleaner");
		 * run.update(String.format("CREATE TRIGGER matchcleaner " +
		 * "AFTER DELETE ON %s FOR EACH ROW BEGIN " +
		 * "DELETE FROM %s WHERE user1 IS NULL AND user2 IS NULL; " + "END;",
		 * getTable(User.class), getTable(Match.class)));
		 */
	}

	// =====================================================================
	// Users
	// =====================================================================

	@Override
	public User loginUser(String username, String password) throws SQLException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("empty password");
		}

		return get(User.class, new HavingConstraint(new EqualConstraint("name",
				username, "password", password)));
	}

	@Override
	public User registerUser(String username, String password)
			throws SQLException, IllegalArgumentException {
		logger.info(username + " wants to register.");
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}

		for (String badWord : getBadWords()) {
			if (username.contains(badWord)) {
				throw new IllegalArgumentException(
						"username contains banned word " + badWord);
			}
		}

		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("empty password");
		}

		logger.info(username + " wants to register.");

		User user = new User(username, false);
		insert(user, "password", password);
		return user;
	}

	@Override
	public void changeUserCredentials(User user, String username,
			String password, String confirmation) throws SQLException,
			IllegalArgumentException {
		if ((username == null || username.isEmpty())
				&& (password == null || password.isEmpty())) {
			throw new IllegalArgumentException(
					"neither new username nor password");
		}

		if (username != null && !username.isEmpty()) {
			for (String badWord : getBadWords()) {
				if (username.contains(badWord)) {
					throw new IllegalArgumentException(
							"username contains banned word " + badWord);
				}
			}

			user.setName(username);
		}

		if (password != null && !password.isEmpty()) {
			if (confirmation == null || confirmation.isEmpty()
					|| !password.equals(confirmation)) {
				throw new IllegalArgumentException("new password not confirmed");
			}

			update(user, "password", password);
		} else {
			update(user);
		}
	}

	@Override
	public void updateUser(User user) throws SQLException {
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}

		update(user);
	}

	@Override
	public void removeUser(User user) throws SQLException {
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}
		remove(user);
	}

	@Override
	public User getUser(String username) throws SQLException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}

		return get(User.class, new HavingConstraint(new EqualConstraint("name",
				username)));
	}

	@Override
	public List<User> getUsers() throws SQLException {
		return getMany(User.class);
	}

	@Override
	public List<String> getBadWords() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet res = null;
		List<String> words = new ArrayList<String>();

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			res = stmt.executeQuery("SELECT word FROM BAD_WORDS");

			while (res.next()) {
				words.add(res.getString(0));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (res != null) {
				res.close();
			}
		}

		return words;
	}

	// =====================================================================
	// Questions
	// =====================================================================

	@Override
	public void addQuestion(Question question) throws SQLException {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}

		insert(question);
	}

	@Override
	public void updateQuestion(Question question) throws SQLException {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}

		update(question);
	}

	@Override
	public void removeQuestion(Question question) throws SQLException {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}
		remove(question);
	}

	@Override
	public List<Question> getQuestions() throws SQLException {
		return getMany(Question.class);
	}

	@Override
	public List<Question> getQuestions(int revision) throws SQLException {
		if (revision < 0 || revision > getNewestRevision()) {
			throw new IllegalArgumentException("invalid revision");
		}

		return getMany(Question.class, new HavingConstraint(
				new LessEqualConstraint("revision", revision)));
	}

	// =====================================================================
	// Matches
	// =====================================================================

	@Override
	public void saveMatch(Match match) throws SQLException {
		if (match == null) {
			throw new IllegalArgumentException("null match");
		}

		if (match.getId() == 0) {
			insert(match);

			for (Round r : match.getRounds()) {
				insert(r);

				for (Answer a : r.getAnswers()) {
					insert(a);
				}
			}
		} else {
			for (Round r : match.getRounds()) {
				for (Answer a : r.getAnswers()) {
					update(a);
				}
			}
		}
	}

	@Override
	public List<Match> getRunningMatches(User user) throws SQLException {
		// get all user's matches
		List<Match> matches = getMany(Match.class, new HavingConstraint(
				new EqualConstraint("user1", user.getId())));

		matches.addAll(getMany(Match.class, new HavingConstraint(
				new EqualConstraint("user2", user.getId()))));

		for (Match m : matches) {
			// set rounds
			List<Round> rounds = getMany(Round.class, new HavingConstraint(
					new EqualConstraint("matchId", m.getId())));

			for (Round r : rounds) {
				// set answers
				List<Answer> answers = getMany(
						Answer.class,
						new HavingConstraint(new EqualConstraint("roundId", r
								.getId())));

				r.setAnswers(answers);
			}

			m.setRounds(rounds);
		}

		// return only running matches
		List<Match> running = new ArrayList<Match>();
		for (Match m : matches) {
			if (!m.isFinished()) {
				// set users
				m.setUser1(get(User.class, new HavingConstraint(
						new EqualConstraint("id", m.getUserId1()))));

				m.setUser2(get(User.class, new HavingConstraint(
						new EqualConstraint("id", m.getUserId2()))));

				running.add(m);
			}
		}

		return running;
	}

	public void saveChallenge(Challenge challenge) throws SQLException {
		insert(challenge);
	}

	public List<Challenge> getChallenges(User user) throws SQLException {
		return getMany(Challenge.class, new HavingConstraint(new EqualConstraint("toId",
				user.getId())));
	}
	
	public void removeChallenge(Challenge challenge) throws SQLException {
		remove(challenge);
	}

	// =====================================================================
	// Entity utils
	// =====================================================================

	private int getNewestRevision() throws SQLException {
		List<Integer> result = run.query("SELECT MAX(revision) FROM "
				+ getTable(Question.class), new BeanListHandler<Integer>(
				Integer.class));
		return result.size() > 0 ? result.get(0) : 0;
	}

	// =====================================================================
	// Generic utils
	// =====================================================================

	private void insert(Object obj, Object... objects) throws SQLException {
		InsertQueryBuilder qb = new InsertQueryBuilder(getTable(obj));
		buildQuery(obj, qb, objects);

		if (qb.hasValues()) {
			String sql = qb.getQuery();
			logger.info(sql);

			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet generatedKeys = null;
			try {
				conn = dataSource.getConnection();
				stmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stmt.executeUpdate();

				generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					int insertId = generatedKeys.getInt(1);
					obj.getClass().getDeclaredMethod("setId", int.class)
							.invoke(obj, insertId);
					logger.info(obj + " inserted with id " + insertId);
				}
			} catch (SQLException e) {
				throw e;
			} catch (Exception e) {
				throw new SQLException("Object's insert id couldn't be set!", e);
			} finally {
				if (generatedKeys != null) {
					generatedKeys.close();
				}

				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			}
		}
	}

	private void update(Object obj, Object... objects) throws SQLException {
		UpdateQueryBuilder qb = new UpdateQueryBuilder(getTable(obj),
				getId(obj));
		buildQuery(obj, qb, objects);

		if (qb.hasValues()) {
			String sql = qb.getQuery();
			logger.info(sql);
			run.update(sql);
		}
	}

	private void buildQuery(Object obj, QueryBuilder qb, Object... objects)
			throws SQLException {
		for (int i = 0, j = 0; j < objects.length / 2; i += 2, j++) {
			qb.appendField(objects[i].toString());
			qb.appendValue(objects[i + 1]);
		}

		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);

			if (!f.getType().isPrimitive() && !f.getType().equals(String.class)) {
				continue;
			}

			qb.appendField(f.getName());

			try {
				qb.appendValue(f.get(obj));
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"couldn't get value of field " + f.getName(), e);
				throw new SQLException("Object couldn't be inserted!", e);
			}
		}
	}

	private void remove(Object obj)
			throws SQLException {
		if (0 == run.update(String.format("DELETE FROM %s WHERE id=%d",
				getTable(obj), getId(obj)))) {
			throw new SQLException("no row updated");
		}
	}

	private <T> T get(Class<T> clazz, Constraint... constraints)
			throws SQLException {
		List<T> results = getMany(clazz, constraints);

		if (results.size() == 1) {
			return results.get(0);
		} else if (results.size() > 1) {
			throw new SQLException("ambiguous id");
		} else {
			return null;
		}
	}

	private <T> List<T> getMany(Class<T> clazz, Constraint... constraints)
			throws SQLException {
		return run.query(String.format("SELECT * FROM %s %s", getTable(clazz),
				Constraint.toString(constraints)),
				new BeanListHandler<T>(clazz));
	}

	// =====================================================================
	// Helpers
	// =====================================================================

	private Field getIdField(Object obj) throws SQLException {
		try {
			Field idField = obj.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			return idField;
		} catch (Exception e) {
			throw new SQLException("couldn't get object id", e);
		}
	}

	private int getId(Object obj) throws SQLException {
		try {
			return getIdField(obj).getInt(obj);
		} catch (Exception e) {
			throw new SQLException("couldn't get object id", e);
		}
	}

	private static <T> String getTable(Class<T> clazz) {
		TableAlias alias = clazz.getAnnotation(TableAlias.class);

		if (alias != null) {
			return alias.table();
		}

		return clazz.getSimpleName().toUpperCase();
	}

	private static String getTable(Object obj) {
		return getTable(obj.getClass());
	}
}
