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
	public static final Persistence persistence;
	public static final NetUtils net;
	public static final ServerDirectory serverDir;

	static {
		Persistence p = null;
		ServerDirectory s = null;
		NetUtils n = null;

		try {
			n = NetUtils.getInstance();
			p = Data.getInstance();
			s = new ServerDirectory(new ServerSocket(Config.get()
					.getInt("PORT")), p, n);
		} catch (SQLException e) {
			throw new RuntimeException("Database error!", e);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Server is misconfigured!", e);
		} catch (IOException e) {
			throw new RuntimeException("Error creating server socket!", e);
		}
		
		persistence = p;
		serverDir = s;
		net = n;
	}

	/**
	 * Initiiates a thread running a server directory.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		new Thread(serverDir).start();
	}

}