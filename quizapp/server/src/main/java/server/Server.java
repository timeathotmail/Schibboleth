package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	 * Initiiates a thread running a server directory.
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		try {
			new Thread(new ServerDirectory(new ServerSocket(NetUtils.PORT))).start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error creating server socket", e);
		}
	}
}