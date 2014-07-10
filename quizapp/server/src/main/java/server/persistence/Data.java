package server.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;

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
		Connection conn = null;

		try {
			Class.forName(JDBC_DRIVER);
			dataSource.setDatabaseName(DB_NAME);
			dataSource.setUser(DB_USER);
			dataSource.setPassword(DB_PASS);
			dataSource.setUrl(DB_URL);
			conn = dataSource.getConnection();
			run = new QueryRunner(dataSource);
			checkDatabaseStructure(conn);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Database driver error!", e);
			throw new SQLException("Couldn't register JDBC driver!");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Asserts that the needed database and tables exist.
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void checkDatabaseStructure(Connection conn) throws SQLException {
		logger.info("checking database structure...");
		ResultSet rs = null;

		try {
			// check if database exists
			boolean dbExists = false;
			rs = conn.getMetaData().getCatalogs();
			while (rs.next()) {
				if (DB_NAME.equals(rs.getString(1))) {
					dbExists = true;
					break;
				}
			}
			rs.close();

			if (!dbExists) {
				logger.info("no database detected, creating...");
				run.update("CREATE DATABASE " + DB_NAME);
				logger.info("database created.");
			}

			// get existing tables
			logger.info("checking tables...");
			List<String> tables = new ArrayList<String>();
			DatabaseMetaData md = conn.getMetaData();
			rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
				tables.add(rs.getString(3));
			}

			// add missing tables
			if (!tables.contains("beispiel")) {
				logger.info("creating table beispiel...");
				// TODO
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
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

	private int getNewestRevision() {
		// TODO
		return 0;
	}
}
