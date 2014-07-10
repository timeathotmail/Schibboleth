package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.persistence.Data;
import server.persistence.IPersistence;
import common.net.NetUtils;

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
	private static IPersistence persistence;

	/**
	 * Initiiates a thread running a server directory.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		try {
			persistence = Data.getInstance();
			new Thread(new ServerDirectory(new ServerSocket(NetUtils.PORT),
					persistence)).start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error creating server socket", e);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "error connecting to database", e);
		}
	}
}