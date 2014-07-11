package server.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import server.ServerProperties;
import server.persistence.constraints.Constraint;
import server.persistence.constraints.EqualConstraint;
import server.persistence.constraints.LessEqualConstraint;
import server.persistence.utils.InsertQueryBuilder;
import server.persistence.utils.QueryBuilder;
import server.persistence.utils.UpdateQueryBuilder;
import common.entities.*;
import common.entities.annotations.*;

/**
 * Provides database access.
 * 
 * @author Tim Wiechers
 */
public class Data implements Persistence {
	/**
	 * 
	 */
	private static final String JDBC_DRIVER = ServerProperties
			.get("JDBC_DRIVER");
	/**
	 * 
	 */
	private static final String DB_URL = ServerProperties.get("DB_URL");
	/**
	 * 
	 */
	private static final String DB_NAME = ServerProperties.get("DB_NAME");
	/**
	 * 
	 */
	private static final String DB_USER = ServerProperties.get("DB_USER");
	/**
	 * 
	 */
	private static final String DB_PASS = ServerProperties.get("DB_PASS");

	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Data");
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
		if (instance == null)
			instance = new Data();

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
			dataSource.setUser(DB_USER);
			dataSource.setPassword(DB_PASS);
			dataSource.setUrl(DB_URL);
			run = new QueryRunner(dataSource);
			checkDatabaseStructure();

			// TODO nur zum Testen
			insert(new User("bob", false));
			User bob = getUser("bob");
			System.out.println(bob);

			System.out.println(getUsers().get(0));

			bob.setName("adolf");
			update(bob);
			bob = getUser("adolf");
			System.out.println(bob);

		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Database driver error!", e);
			throw new SQLException("Couldn't register JDBC driver!");
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

		run.update("DROP DATABASE " + DB_NAME); // TODO nur zum Testen
		run.update("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
		dataSource.setUrl(DB_URL + DB_NAME);

		run.update("CREATE TABLE IF NOT EXISTS USER("
				+ "id   INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "isAdmin BOOLEAN  NOT NULL," // FIXME immer false
				+ "name VARCHAR(30)  NOT NULL UNIQUE)");

		run.update("CREATE TABLE IF NOT EXISTS QUESTION("
				+ "id           INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "category     VARCHAR(50)  NOT NULL,"
				+ "text         VARCHAR(200) NOT NULL,"
				+ "answer1      VARCHAR(30)  NOT NULL,"
				+ "answer2      VARCHAR(30)  NOT NULL,"
				+ "answer3      VARCHAR(30)  NOT NULL,"
				+ "answer4      VARCHAR(30)  NOT NULL,"
				+ "correctIndex TINYINT UNSIGNED,"
				+ "revision     SMALLINT UNSIGNED NOT NULL)");

		run.update("CREATE TABLE IF NOT EXISTS MATCH__("
				+ "id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "user1   INT UNSIGNED,"
				+ "user2   INT UNSIGNED," // FIXME nutzer können gleich sein
				+ "points1 INT UNSIGNED NOT NULL,"
				+ "points2 INT UNSIGNED NOT NULL,"
				+ "FOREIGN KEY(user1) REFERENCES USER(id) ON DELETE CASCADE,"
				+ "FOREIGN KEY(user2) REFERENCES USER(id) ON DELETE CASCADE)");
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
		// TODO check password
		return getUser(new EqualConstraint("name", username));
	}

	@Override
	public User registerUser(String username, String password)
			throws SQLException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("empty password");
		}

		// TODO insert password
		User user = new User(username, false);
		insert(user);
		return user;
	}

	@Override
	public void changeUserCredentials(String username, String password,
			String confirmation) throws SQLException, IllegalArgumentException {
		if ((username == null || username.isEmpty())
				&& (password == null || password.isEmpty())) {
			throw new IllegalArgumentException(
					"neither new username nor password");
		}
		if (confirmation == null || confirmation.isEmpty()
				|| !password.equals(confirmation)) {
			throw new IllegalArgumentException("new password not confirmed");
		}

		User user = getUser(username);
		// TODO set password
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
		// TODO Auto-generated method stub
	}

	@Override
	public User getUser(String username) throws SQLException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}

		return getUser(new EqualConstraint("name", username));
	}

	@Override
	public List<User> getUsers() throws SQLException {
		return getMany(User.class);
	}

	@Override
	public List<User> getRankedUsers(int offset, int length) {
		if (offset < 0) {
			throw new IllegalArgumentException("invalid offset");
		}
		if (length < 0) {
			throw new IllegalArgumentException("invalid length");
		}
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
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

		return getMany(Question.class, new LessEqualConstraint("revision",
				revision));
	}

	// =====================================================================
	// Matches
	// =====================================================================

	@Override
	public void saveMatch(Match match) throws SQLException {
		if (match == null) {
			throw new IllegalArgumentException("null matchn");
		}

		insert(match);
	}

	// =====================================================================
	// Entity utils
	// =====================================================================

	private int getNewestRevision() {
		// TODO
		return 0;
	}

	private User getUser(Constraint... constraints) throws SQLException {
		User user = get(User.class, constraints);

		// set user's stats
		String sql = String
				.format("SELECT SUM(_points) AS pointCount, _win AS winCount, COUNT(*) AS matchCount FROM ("
						+ "SELECT IF(user1=%d, points1, points2) AS _points, "
						+ "IF((SELECT(_points)) >= points1 AND (SELECT(_points)) >= points2,0,1) AS _win "
						+ "FROM %s WHERE user1=%d OR user2=%d) AS _",
						user.getId(), getTable(Match.class), user.getId(),
						user.getId());

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				user.setMatchCount(rs.getInt("matchCount"));
				user.setWinCount(rs.getInt("winCount"));
				user.setPointCount(rs.getInt("PointCount"));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}

			if (rs != null) {
				rs.close();
			}

			conn.close();
		}

		return user;
	}

	// =====================================================================
	// Generic utils
	// =====================================================================

	private void insert(Object obj) throws SQLException {
		InsertQueryBuilder qb = new InsertQueryBuilder(getTable(obj));
		query(obj, qb);

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
					getIdField(obj).set(obj, insertId);
					logger.info(obj + " inserted with id " + insertId);
				}
			} catch (Exception e) {
				throw new SQLException("Object's insert id couldn't be set!");
			} finally {
				if (generatedKeys != null) {
					generatedKeys.close();
				}

				if (stmt != null) {
					stmt.close();
				}

				if(conn != null) {
					conn.close();
				}
			}
		}
	}

	private void update(Object obj) throws SQLException {
		UpdateQueryBuilder qb = new UpdateQueryBuilder(getTable(obj),
				getId(obj));
		query(obj, qb);

		if (qb.hasValues()) {
			String sql = qb.getQuery();
			logger.info(sql);
			run.update(sql);
		}
	}

	private void query(Object obj, QueryBuilder qb) throws SQLException {
		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);

			// unpersisted field
			if (f.getAnnotation(NotPersisted.class) != null) {
				continue;
			}

			// persisted field
			ColumnAlias alias;
			String field;
			if ((alias = f.getAnnotation(ColumnAlias.class)) != null) {
				field = alias.column();
			} else {
				field = f.getName();
			}

			qb.appendField(field);

			try {
				if (f.getType().equals(String.class)) {
					qb.appendValue(String.format("'%s'", f.get(obj).toString()));
				} else {
					qb.appendValue(f.get(obj).toString());
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"couldn't get value of field " + f.getName(), e);
				throw new SQLException("Object couldn't be inserted!");
			}
		}
	}

	private <T> T get(Class<T> clazz, Constraint... constraints)
			throws SQLException {
		List<T> results = run.query(String.format("SELECT * FROM %s %s",
				getTable(clazz), getWhereClause(constraints)),
				new BeanListHandler<T>(clazz));

		if (results.size() == 1) {
			return results.get(0);
		} else if (results.size() > 1) {
			throw new SQLException("ambiguous id");
		} else {
			throw new SQLException("no result");
		}
	}

	private <T> List<T> getMany(Class<T> clazz, Constraint... constraints)
			throws SQLException {
		return run.query(String.format("SELECT * FROM %s %s", getTable(clazz),
				getWhereClause(constraints)), new BeanListHandler<T>(clazz));
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
			throw new SQLException("couldn't get object id");
		}
	}

	private int getId(Object obj) throws SQLException {
		try {
			return getIdField(obj).getInt(obj);
		} catch (Exception e) {
			throw new SQLException("couldn't get object id");
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

	private static <T extends Constraint> String getWhereClause(
			T... constraints) {
		StringBuilder sb = new StringBuilder(" WHERE ");

		for (T constraint : constraints) {
			sb.append(constraint + " AND ");
		}

		return sb.substring(0, sb.length() - 5);
	}
}
