package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	//public static final String IP = "127.0.0.1";
	public static final int PORT = 11111;
	//public static final int MSG_LENGTH = 1024;

	private static final Logger logger = Logger.getLogger("Server");
	
	public static void main(String[] args) {
		try {
			new Thread(new ServerDirectory(new ServerSocket(PORT))).start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error creating server socket", e);
		}
	}
}