package server;

import java.net.Socket;

import server.persistence.IPersistence;
import common.entities.User;
import common.net.NetUtils;
import common.net.requests.AuthRequest;
import common.net.requests.LogoutRequest;
import common.net.responses.AuthResponse;
import common.net.responses.UserListChangedResponse;

public class ServerInbox extends AbstractServerInbox {
	private final IPersistence persistence;

	public ServerInbox(Socket client, ServerDirectory serverDir,
			IPersistence persistence) {
		super(client, serverDir);
		this.persistence = persistence;
	}

	@Override
	protected void process(Socket client, AuthRequest obj) {
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

	@Override
	protected void process(Socket client, LogoutRequest obj) {
		serverDir.RemoveClient(client);
		// TODO remove open challenges
		// TODO inform other clients
	}
}