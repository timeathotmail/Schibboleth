package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.net.NetUtils;

public class Server {

	private static final Logger logger = Logger.getLogger("Server");
	
	public static void main(String[] args) {
		try {
			new Thread(new ServerDirectory(new ServerSocket(NetUtils.PORT))).start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error creating server socket", e);
		}
	}
}