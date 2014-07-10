package server.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.entities.*;

/**
 * Provides database access.
 * 
 * @author Tim Wiechers
 */
public class Data implements IPersistence {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/";
	private static final String DB_NAME = "QUIZAPP";
	private static final String USER = "root";
	private static final String PASS = "";

	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Data");
	/**
	 * Singleton instance
	 */
	private static Data instance;

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
		ResultSet resultSet = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// check if database exists
			resultSet = conn.getMetaData().getCatalogs();
			while (resultSet.next()) {
				if (DB_NAME.equals(resultSet.getString(1))) {
					checkTableStructure(conn);
					return;
				}
			}
			logger.info("no database detected, creating...");
			stmt = conn.createStatement();
			stmt.execute("CREATE DATABASE " + DB_NAME);
			logger.info("database created.");
			checkTableStructure(conn);

		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error connecting to database", e);
			throw new SQLException("Couldn't register JDBC driver.");

		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	private void checkTableStructure(Connection conn) throws SQLException {
		// get existing tables
		List<String> tables = new ArrayList<String>();
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
			tables.add(rs.getString(3));
		}

		// add missing tables
		if (!tables.contains("beispiel")) {
			logger.info("creating table beispiel...");
			// TODO
		}
	}

	// =====================================================================

	public User loginUser(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	// Olga:
	// hier soll eine Exception geworfen werden, falls einer der beiden
	// Parameter
	// inkorrekt ist, zB null, leerer String oder zu kurz/zu lang
	// Ich teste erstmal auf null und leerer String.
	// Dazu kommt dass ein Name nicht mehr als einmal benutzt werden darf. Das
	// teste ich auch.
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
	public List<Integer> getQuestionIds(int revision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Question> getQuestions(int revision) {
		// TODO Auto-generated method stub
		return null;
	}
}
