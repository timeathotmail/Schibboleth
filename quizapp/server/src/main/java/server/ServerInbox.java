package server;

import java.net.Socket;

import server.persistence.IPersistence;
import common.entities.User;
import common.net.NetUtils;
import common.net.requests.AuthRequest;
import common.net.requests.LogoutRequest;
import common.net.responses.AuthResponse;
import common.net.responses.UserListChangedResponse;

public class ServerInbox implements Runnable {
	private final Socket client;
	private final ServerDirectory serverDir;
	private final IPersistence persistence;

	protected ServerInbox(final Socket client, ServerDirectory serverDir, IPersistence persistence) {
		this.client = client;
		this.serverDir = serverDir;
		this.persistence = persistence;
	}

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

	private void process(Socket client, LogoutRequest obj) {
		serverDir.RemoveClient(client);
		// TODO remove open challenges
		// TODO inform other clients
	}
}
