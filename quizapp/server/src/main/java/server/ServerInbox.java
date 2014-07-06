package server;

import java.net.Socket;

import server.persistence.IPersistence;
import common.entities.User;
import common.net.NetUtils;
import common.net.requests.AuthRequest;
import common.net.requests.LogoutRequest;
import common.net.responses.AuthResponse;
import common.net.responses.UserListChangedResponse;

/**
 * Runnable processing incoming client messages.
 * 
 * @author Tim Wiechers
 */
public class ServerInbox implements Runnable {
	/**
	 * Client's socket.
	 */
	private final Socket client;
	/**
	 * Server's directory.
	 */
	private final ServerDirectory serverDir;
	/**
	 * Database access.
	 */
	private final IPersistence persistence;

	/**
	 * Creates an instance.
	 * @param client client's socket
	 * @param serverDir server's directory
	 * @param persistence persistence instance
	 */
	public ServerInbox(final Socket client, ServerDirectory serverDir, IPersistence persistence) {
		this.client = client;
		this.serverDir = serverDir;
		this.persistence = persistence;
	}

	/**
	 * Waits for a client message, then tries to map and process it.
	 */
	public void run() {
		try {
			while (true) {
				String json = NetUtils.read(client);
				AuthRequest obj = NetUtils.fromJson(json, AuthRequest.class);
				if (obj != null) {
					process(client, obj);
				}

				LogoutRequest obj2 = NetUtils.fromJson(json,
						LogoutRequest.class);
				if (obj2 != null) {
					process(client, obj2);
				}

				// TODO: rest
			}
		} catch (RuntimeException e) {
			serverDir.RemoveClient(client);
		}
	}

	/**
	 * Process an AuthRequest.
	 * @param client sending client
	 * @param obj the request
	 */
	private void process(Socket client, AuthRequest obj) {
		// login or register user
		User user;
		if (obj.wantsToRegister()) {
			user = persistence.registerUser(obj.getUsername(),
					obj.getPassword());
		} else {
			user = persistence.loginUser(obj.getUsername(), obj.getPassword());
		}

		// on success
		if (user != null) {
			// send user the list of other users
			NetUtils.send(client,
					new AuthResponse(true, serverDir.getActiveUsers()));
			// inform other users about the new client
			NetUtils.send(serverDir.getActiveSockets(),
					new UserListChangedResponse(true, user));
			// connect username and socket in the server directory
			serverDir.idClient(client, user);
		} else {
			NetUtils.send(client, new AuthResponse(false, null));
		}
	}

	/**
	 * Process a LogoutRequest
	 * @param client sending client
	 * @param obj the request
	 */
	private void process(Socket client, LogoutRequest obj) {
		serverDir.RemoveClient(client);
		// TODO remove open challenges
		// TODO inform other clients
	}
}
