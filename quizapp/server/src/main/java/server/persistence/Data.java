package server.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import server.ServerProperties;
import common.entities.*;

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

		run.update("DROP DATABASE " + DB_NAME);
		run.update("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
		dataSource.setUrl(DB_URL + DB_NAME);

		run.update("CREATE TABLE IF NOT EXISTS USER("
				+ "id   INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "isAdmin BOOLEAN  NOT NULL," // FIXME immer false
				+ "name VARCHAR(30)  NOT NULL UNIQUE)");

		run.update("CREATE TABLE IF NOT EXISTS QUESTION("
				+ "id           INT UNSIGNED      PRIMARY KEY AUTO_INCREMENT,"
				+ "category     VARCHAR(50)       NOT NULL,"
				+ "text         VARCHAR(200)      NOT NULL,"
				+ "answer1      VARCHAR(30)       NOT NULL,"
				+ "answer2      VARCHAR(30)       NOT NULL,"
				+ "answer3      VARCHAR(30)       NOT NULL,"
				+ "answer4      VARCHAR(30)       NOT NULL,"
				+ "correctIndex TINYINT UNSIGNED  CHECK(correctIndex < 4),"
				+ "revision     SMALLINT UNSIGNED NOT NULL)");

		run.update("CREATE TABLE IF NOT EXISTS MATCH_("
				+ "id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,"
				+ "user1   INT UNSIGNED REFERENCES USER(id) ON DELETE CASCADE,"
				+ "user2   INT UNSIGNED REFERENCES USER(id) ON DELETE CASCADE,"
				+ "points1 INT UNSIGNED NOT NULL,"
				+ "points2 INT UNSIGNED NOT NULL)");

		User user = new User("Bob", true);
		insert(user);
		System.out.println(user.getId());
		User sameUser = getUser(user.getId());
		System.out.println(sameUser.getName());
		System.out.println(sameUser.isAdmin());
	}

	// =====================================================================
	// Persistence implementation
	// =====================================================================

	@Override
	public User loginUser(String username, String password) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("empty password");
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User registerUser(String username, String password) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("empty password");
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUser(String username) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("empty username");
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public boolean addQuestion(Question question) {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQuestion(Question question) {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeQuestion(Question question) {
		if (question == null) {
			throw new IllegalArgumentException("null question");
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Question> getQuestions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveMatch(Match match) {
		if (match == null) {
			throw new IllegalArgumentException("null matchn");
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeUserCredentials(String username, String password,
			String confirmation) {
		if ((username == null || username.isEmpty())
				&& (password == null || password.isEmpty())) {
			throw new IllegalArgumentException(
					"neither new username nor password");
		}
		if (password != null && !password.isEmpty()) {
			if (confirmation == null || confirmation.isEmpty()) {
				throw new IllegalArgumentException("new password not confirmed");
			} else if (!password.equals(confirmation)) {
				return false;
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Question> getQuestions(int revision) {
		if (revision < 0 || revision > getNewestRevision()) {
			throw new IllegalArgumentException("invalid revision");
		}

		// TODO Auto-generated method stub
		return null;
	}

	// =====================================================================
	// Utils
	// =====================================================================

	private void insert(Object obj) throws SQLException {
		StringBuilder fields = new StringBuilder("INSERT INTO " + getTable(obj)
				+ " (");
		StringBuilder values = new StringBuilder("VALUES (");
		Field id = null;
		int numberOfFields = 0;

		// TODO ist echt gut lesbar nach auto formatieren
		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (!f.getName().equals("id")) {
				if (f.getAnnotation(NotPersisted.class) == null) {
					fields.append(f.getName() + ",");
					numberOfFields++;
					try {
						if(!f.getType().equals(boolean.class)) {
							values.append("'" + f.get(obj) + "'");
						} else {
							values.append(f.get(obj));
						}
						values.append(",");
					} catch (Exception e) {
						logger.log(
								Level.SEVERE,
								"insert: couldn't get value of field "
										+ f.getName(), e);
						throw new SQLException("Object couldn't be inserted!");
					}
				}
			} else {
				id = f;
			}
		}

		if (numberOfFields > 0) {
			// remove last commas
			fields.deleteCharAt(fields.length() - 1);
			values.deleteCharAt(values.length() - 1);

			// close statement parts
			fields.append(") ");
			values.append(")");

			String sqlInsert = fields.toString() + values.toString();
			logger.info(sqlInsert);

			Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlInsert,
					Statement.RETURN_GENERATED_KEYS);
			int insertId = stmt.executeUpdate();

			try {
				id.set(obj, insertId);
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"insert: object's insert id couldn't be set", e);
				throw new SQLException("Object's insert id couldn't be set!");
			} finally {
				if (stmt != null) {
					stmt.close();
				}

				conn.close();
			}
		}
	}

	private User getUser(int id) throws SQLException {
		User user = get(id, User.class);
		// TODO winCount, matchCount, pointCount
		return user;
	}

	private <T> T get(int id, Class<T> clazz) throws SQLException {
		List<T> results = run.query("SELECT * FROM " + getTable(clazz) + " WHERE id=" + id,
				new BeanListHandler<T>(clazz));
		
		if(results.size() == 1) {
			return results.get(0);
		} else if(results.size() > 1) {
			throw new SQLException("ambiguous id");
		} else {
			throw new SQLException("no result");
		}
	}

	private <T> String getTable(Class<T> clazz) {
		return clazz.getSimpleName().toUpperCase();
	}

	private String getTable(Object obj) {
		return obj.getClass().getSimpleName().toUpperCase();
	}
	
	private int getNewestRevision() {
		// TODO
		return 0;
	}
}
