package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import server.net.ServerDirectory;
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
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Server");
	/**
	 * Persistence.
	 */
	private static Persistence persistence;

	/**
	 * Initiiates a thread running a server directory.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		try {
			persistence = Data.getInstance();
			new Thread(new ServerDirectory(new ServerSocket(Config.get()
					.getInt("PORT")), persistence, NetUtils.getInstance()))
					.start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error creating server socket!", e);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error connecting to database!", e);
		} catch (ConfigurationException e) {
			logger.log(Level.SEVERE, "Server is misconfigured!", e);
		}
	}
}