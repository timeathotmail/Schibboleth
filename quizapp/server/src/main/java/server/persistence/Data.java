package server.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import server.Server;
import server.persistence.constraints.*;
import server.persistence.query.InsertQueryBuilder;
import server.persistence.query.QueryBuilder;
import server.persistence.query.UpdateQueryBuilder;
import common.entities.*;
import common.entities.Question.Category;
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
	 * The database file.
	 */
	private static String DB_FILE;
	/**
	 * Singleton instance.
	 */
	private static Data instance;
	/**
	 * QueryRunner.
	 */
	private final QueryRunner run = new QueryRunner();
	/**
	 * The established connection to the database file.
	 */
	private final Connection conn;

	// =====================================================================
	// Init database connection
	// =====================================================================

	/**
	 * @return the singleton instance
	 * @throws SQLException
	 *             if a connection can't be established
	 */
	public static Data getInstance() throws SQLException {
		if (instance == null) {
			// change log output format
			logger.setUseParentHandlers(false);
			Handler conHdlr = new ConsoleHandler();
			conHdlr.setFormatter(new Formatter() {
				public String format(LogRecord record) {
					return record.getLevel() + ": "
							+ record.getSourceClassName() + "."
							+ record.getSourceMethodName() + ": "
							+ record.getMessage() + "\n";
				}
			});
			logger.addHandler(conHdlr);

			// get db file name
			try {
				DB_FILE = Config.get().get("DB_FILE");
			} catch (ConfigurationException e) {
				throw new SQLException("Server is misconfigured!", e);
			}

			// create singleton instance
			instance = new Data();
		}

		return instance;
	}

	/**
	 * Creates an instance.
	 * 
	 * @throws SQLException
	 *             if a connection can't be established
	 */
	private Data() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
			checkDatabaseStructure(); // in case of a new file

		} catch (ClassNotFoundException e) {
			throw new SQLException("Couldn't register sqlite JDBC driver!", e);
		}
	}

	/**
	 * Asserts that the needed database and tables exist.
	 * 
	 * @throws SQLException
	 *             if any table creation failed
	 */
	private void checkDatabaseStructure() throws SQLException {
		logger.info("checking database structure...");

		run.update(conn, String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "name VARCHAR(30)  NOT NULL UNIQUE, "
				+ "password VARCHAR (30) NOT NULL, "
				+ "isAdmin BOOLEAN  NOT NULL)", getTable(User.class)));

		run.update(conn, String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "category     VARCHAR(50)  NOT NULL,"
				+ "text         VARCHAR(200) NOT NULL,"
				+ "answer1      VARCHAR(30)  NOT NULL,"
				+ "answer2      VARCHAR(30)  NOT NULL,"
				+ "answer3      VARCHAR(30)  NOT NULL,"
				+ "answer4      VARCHAR(30)  NOT NULL,"
				+ "correctIndex TINYINT UNSIGNED,"
				+ "revision     SMALLINT UNSIGNED NOT NULL)",
				getTable(Question.class)));

		run.update(conn, String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "user1   INT UNSIGNED," + "user2   INT UNSIGNED,"
				+ "FOREIGN KEY(user1) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL,"
				+ "FOREIGN KEY(user2) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL)", getTable(Match.class),
				getTable(User.class), getTable(User.class)));

		run.update(conn, String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "matchId INT UNSIGNED ,"
				+ "FOREIGN KEY(matchId) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL" + ")", getTable(Round.class),
				getTable(Match.class)));

		run.update(conn, String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "roundId INT UNSIGNED ," + "questionId INT UNSIGNED ,"
				+ "answerIndex1 TINYINT UNSIGNED, "
				+ "answerIndex2 TINYINT UNSIGNED, "
				+ "FOREIGN KEY(roundId) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL, "
				+ "FOREIGN KEY(questionId) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL)", getTable(Answer.class),
				getTable(Round.class), getTable(Question.class)));

		run.update(conn, String.format("CREATE TABLE IF NOT EXISTS %s("
				+ "fromId INT UNSIGNED, " + "toId   INT UNSIGNED, "
				+ "FOREIGN KEY(fromId) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL, "
				+ "FOREIGN KEY(toId) REFERENCES %s(rowid) "
				+ "ON DELETE SET NULL)", getTable(Challenge.class),
				getTable(User.class), getTable(User.class)));

		run.update(
				conn,
				String.format("CREATE TABLE IF NOT EXISTS BAD_WORDS(word VARCHAR(100))"));
	}

	// =====================================================================
	// Users
	// =====================================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#loginUser(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User loginUser(String username, String password) throws SQLException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("empty password");
		}

		return get(User.class, new WhereConstraint(new EqualConstraint("name",
				username, "password", password)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#registerUser(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User registerUser(String username, String password)
			throws SQLException, IllegalArgumentException {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * server.persistence.Persistence#changeUserCredentials(common.entities.
	 * User, java.lang.String, java.lang.String, java.lang.String)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#updateUser(common.entities.User)
	 */
	@Override
	public void updateUser(User user) throws SQLException {
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}

		update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#removeUser(common.entities.User)
	 */
	@Override
	public void removeUser(User user) throws SQLException {
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}
		remove(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String username) throws SQLException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}

		return get(User.class, new WhereConstraint(new EqualConstraint("name",
				username)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#getUsers()
	 */
	@Override
	public List<User> getUsers() throws SQLException {
		return getMany(User.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#getBadWords()
	 */
	@Override
	public List<String> getBadWords() throws SQLException {
		Statement stmt = null;
		ResultSet res = null;
		List<String> words = new ArrayList<String>();

		try {
			stmt = conn.createStatement();
			res = stmt.executeQuery("SELECT word FROM BAD_WORDS");

			while (res.next()) {
				words.add(res.getString(0));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#addQuestion(common.entities.Question)
	 */
	@Override
	public void addQuestion(Question question) throws SQLException {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}

		insert(question);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * server.persistence.Persistence#updateQuestion(common.entities.Question)
	 */
	@Override
	public void updateQuestion(Question question) throws SQLException {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}

		update(question);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * server.persistence.Persistence#removeQuestion(common.entities.Question)
	 */
	@Override
	public void removeQuestion(Question question) throws SQLException {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}
		remove(question);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#getQuestions()
	 */
	@Override
	public List<Question> getQuestions() throws SQLException {
		return getMany(Question.class);
	}

	// =====================================================================
	// Matches
	// =====================================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#saveMatch(common.entities.Match)
	 */
	@Override
	public void saveMatch(Match match) throws SQLException {
		if (match == null) {
			throw new IllegalArgumentException("null match");
		}

		if (match.getRowId() == 0) { // insert new match
			// setting random questions for match
			List<Question> questions = new ArrayList<Question>();

			// get and shuffle all categories
			List<Category> categories = new ArrayList<Category>(
					Arrays.asList(Category.values()));
			Collections.shuffle(categories);

			//
			for (int i = 0; i < Server.ROUNDS_PER_MATCH; i++) {
				questions.addAll(getMany(Question.class,
						new WhereConstraint(new EqualConstraint("category",
								categories.get(i % categories.size()))),
						new OrderByConstraint(null, "RAND()"),
						new LimitedConstraint(Server.QUESTIONS_PER_ROUND, 0)));
			}

			match.setQuestions(questions, Server.QUESTIONS_PER_ROUND);
			insert(match);

			for (Round r : match.getRounds()) {
				insert(r);

				for (Answer a : r.getAnswers()) {
					insert(a);
				}
			}
		} else { // updating running match
			for (Round r : match.getRounds()) {
				for (Answer a : r.getAnswers()) {
					update(a);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * server.persistence.Persistence#getRunningMatches(common.entities.User)
	 */
	@Override
	public List<Match> getRunningMatches(User user) throws SQLException {
		// get all user's matches
		List<Match> matches = getMany(Match.class, new WhereConstraint(
				new EqualConstraint("user1", user.getRowId())));

		matches.addAll(getMany(Match.class, new WhereConstraint(
				new EqualConstraint("user2", user.getRowId()))));

		for (Match m : matches) {
			// set rounds
			List<Round> rounds = getMany(Round.class, new WhereConstraint(
					new EqualConstraint("matchId", m.getRowId())));

			for (Round r : rounds) {
				// set answers
				List<Answer> answers = getMany(
						Answer.class,
						new WhereConstraint(new EqualConstraint("roundId", r
								.getRowId())));

				r.setAnswers(answers);
			}

			m.setRounds(rounds);
		}

		// return only running matches
		List<Match> running = new ArrayList<Match>();
		for (Match m : matches) {
			if (!m.isFinished()) {
				// set users
				m.setUser1(get(User.class, new WhereConstraint(
						new EqualConstraint("rowid", m.getUserId1()))));

				m.setUser2(get(User.class, new WhereConstraint(
						new EqualConstraint("rowid", m.getUserId2()))));

				running.add(m);
			}
		}

		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * server.persistence.Persistence#saveChallenge(common.entities.Challenge)
	 */
	public void saveChallenge(Challenge challenge) throws SQLException {
		insert(challenge);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.Persistence#getChallenges(common.entities.User)
	 */
	public List<Challenge> getChallenges(User user) throws SQLException {
		return getMany(Challenge.class, new WhereConstraint(
				new EqualConstraint("toId", user.getRowId())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * server.persistence.Persistence#removeChallenge(common.entities.Challenge)
	 */
	public void removeChallenge(Challenge challenge) throws SQLException {
		remove(challenge);
	}

	// =====================================================================
	// Generic utils
	// =====================================================================

	/**
	 * Inserts an object.
	 * 
	 * @param obj
	 *            the object to insert
	 * @param fieldsAndValues
	 *            additional field-value pairs to include in the query
	 * @throws SQLException
	 *             if the object cannot be inserted
	 */
	private void insert(Object obj, Object... fieldsAndValues)
			throws SQLException {
		InsertQueryBuilder qb = new InsertQueryBuilder(getTable(obj));
		buildQuery(obj, qb, fieldsAndValues);

		if (qb.hasValues()) {
			String sql = qb.getQuery();
			logger.info(sql);

			PreparedStatement stmt = null;
			ResultSet generatedKeys = null;
			try {
				stmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stmt.executeUpdate();

				generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					int insertId = generatedKeys.getInt(1);
					obj.getClass().getDeclaredMethod("setRowId", int.class)
							.invoke(obj, insertId);
					logger.info(obj + " inserted with rowid " + insertId);
				}
			} catch (SQLException ex) {
				for (Throwable e : ex) {
					if (e instanceof SQLException) {
						logger.log(Level.SEVERE,
								"Throwing SQLException: " + e.getMessage());
					}
				}
				throw ex;
			} catch (Exception e) {
				throw new SQLException(
						"Object's insert rowid couldn't be set!", e);
			} finally {
				if (generatedKeys != null) {
					generatedKeys.close();
				}

				if (stmt != null) {
					stmt.close();
				}
			}
		}
	}

	/**
	 * Updates an object.
	 * 
	 * @param obj
	 *            the object to update
	 * @param fieldsAndValues
	 *            additional field-value pairs to include in the query
	 * @throws SQLException
	 *             if the object cannot be updated
	 */
	private void update(Object obj, Object... fieldsAndValues)
			throws SQLException {
		UpdateQueryBuilder qb = new UpdateQueryBuilder(getTable(obj),
				getRowId(obj));
		buildQuery(obj, qb, fieldsAndValues);

		if (qb.hasValues()) {
			String sql = qb.getQuery();
			logger.info(sql);
			if (0 == run.update(conn, sql)) {
				logger.info("no row updated");
			}
		}
	}

	/**
	 * Adds fields and values to a provided QueryBuilder.
	 * 
	 * @param obj
	 *            the object to build a query for
	 * @param qb
	 *            the QueryBuilder instance
	 * @param fieldsAndValues
	 *            additional field-value pairs to include in the query
	 * @throws SQLException
	 *             if the query couldn't be built
	 */
	private void buildQuery(Object obj, QueryBuilder qb,
			Object... fieldsAndValues) throws SQLException {
		for (int i = 0, j = 0; j < fieldsAndValues.length / 2; i += 2, j++) {
			qb.appendField(fieldsAndValues[i].toString());
			qb.appendValue(fieldsAndValues[i + 1]);
		}

		for (Field f : obj.getClass().getDeclaredFields()) {
			if (f.getName().equals("rowid")
					|| (!f.getType().isPrimitive() && !f.getType().equals(
							String.class))) {
				continue;
			}

			f.setAccessible(true);
			qb.appendField(f.getName());

			try {
				qb.appendValue(f.get(obj));
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"couldn't get value of field " + f.getName(), e);
				throw new SQLException("Couldn't build query!", e);
			}
		}
	}

	/**
	 * Removes an object from the database.
	 * 
	 * @param obj
	 *            the object to remove
	 * @throws SQLException
	 *             if the object wasn't removed
	 */
	private void remove(Object obj) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE rowid=%d",
				getTable(obj), getRowId(obj));
		logger.info(sql);
		if (0 == run.update(conn, sql)) {
			throw new SQLException("no row updated");
		}
	}

	/**
	 * Selects a single entry from the database.
	 * 
	 * @param clazz
	 *            the class of the needed object
	 * @param constraints
	 *            the constraints the entry has to fulfill
	 * @return the object from the database.
	 * @throws SQLException
	 *             if more than one result were selected
	 */
	private <T> T get(Class<T> clazz, Constraint... constraints)
			throws SQLException {
		List<T> results = getMany(clazz, constraints);

		if (results.size() == 1) {
			return results.get(0);
		} else if (results.size() > 1) {
			throw new SQLException("ambiguous select result");
		} else {
			return null;
		}
	}

	/**
	 * Selects multiple entries from the database.
	 * 
	 * @param clazz
	 *            the class of the needed objects
	 * @param constraints
	 *            the constraints the entries have to fulfill
	 * @return a list of objects that fulfill the constraints
	 * @throws SQLException
	 *             if the select query fails
	 */
	private <T> List<T> getMany(Class<T> clazz, Constraint... constraints)
			throws SQLException {
		String sql = String.format("SELECT rowid, * FROM %s %s",
				getTable(clazz), Constraint.toString(constraints));
		logger.info(sql);
		return run.query(conn, sql, new BeanListHandler<T>(clazz));
	}

	// =====================================================================
	// Helpers
	// =====================================================================

	/**
	 * @param obj
	 *            the object to get the field of
	 * @return the rowId field of an object
	 * @throws SQLException
	 *             if there is no rowId field
	 */
	private Field getRowIdField(Object obj) throws SQLException {
		try {
			Field idField = obj.getClass().getDeclaredField("rowid");
			idField.setAccessible(true);
			return idField;
		} catch (Exception e) {
			throw new SQLException("couldn't get object rowid", e);
		}
	}

	/**
	 * @param obj
	 *            the object to get the rowId of
	 * @return the rowId of the object
	 * @throws SQLException
	 *             if object doesn't have a rowId or the value couldn't be
	 *             resolved
	 */
	private int getRowId(Object obj) throws SQLException {
		try {
			return getRowIdField(obj).getInt(obj);
		} catch (Exception e) {
			throw new SQLException("couldn't get object rowid", e);
		}
	}

	/**
	 * @param clazz
	 *            the class to get the table name of
	 * @return the table name for a given class
	 */
	private static <T> String getTable(Class<T> clazz) {
		TableAlias alias = clazz.getAnnotation(TableAlias.class);

		if (alias != null) {
			return alias.table();
		}

		return clazz.getSimpleName().toUpperCase();
	}

	/**
	 * @param obj
	 *            the object to get the table name of
	 * @return the table name for a given object
	 */
	private static String getTable(Object obj) {
		return getTable(obj.getClass());
	}
}
