package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

import javax.naming.ConfigurationException;

import server.persistence.Data;
import server.persistence.Persistence;
import common.net.NetUtils;
import common.utils.Config;

/**
 * Main server class.
 * 
 * @author Tim Wiechers
 */
public class Server {
	/**
	 * Time limit for questions.
	 */
	public static final int TIME_LIMIT;
	/**
	 * Number of questions per round.
	 */
	public static final int QUESTIONS_PER_ROUND;
	/**
	 * Number of rounds per match.
	 */
	public static final int ROUNDS_PER_MATCH;

	/**
	 * Global persistence object.
	 */
	public static final Persistence data;
	/**
	 * Global NetUtils object.
	 */
	public static final NetUtils net;
	/**
	 * Global ServerDirectory object.
	 */
	public static final ServerDirectory dir;

	/**
	 * Static constructor.
	 */
	static {
		try {
			net = NetUtils.getInstance();
			data = Data.getInstance();
			dir = new ServerDirectory(new ServerSocket(Config.get().getInt(
					"PORT")));

			TIME_LIMIT = Config.get().getInt("TIME_LIMIT");
			QUESTIONS_PER_ROUND = Config.get().getInt("QUESTIONS_PER_ROUND");
			ROUNDS_PER_MATCH = Config.get().getInt("ROUNDS_PER_MATCH");
		} catch (SQLException e) {
			throw new RuntimeException("Database error!", e);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Server is misconfigured!", e);
		} catch (IOException e) {
			throw new RuntimeException("Error creating server socket!", e);
		}
	}

	/**
	 * Initiiates a thread running a server directory.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		new Thread(dir).start();
	}

}