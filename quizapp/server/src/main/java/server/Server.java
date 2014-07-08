package server;

import java.io.IOException;
import java.net.ServerSocket;
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

	private static final IPersistence persistence = Data.getInstance();

	/**
	 * Initiiates a thread running a server directory.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		try {
			new Thread(new ServerDirectory(new ServerSocket(NetUtils.PORT),
					persistence)).start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error creating server socket", e);
		}
	}
}