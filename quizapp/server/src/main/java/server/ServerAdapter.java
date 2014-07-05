package server;

import java.net.Socket;

import common.entities.User;
import common.net.Communication;
import common.net.requests.AuthRequest;
import common.net.requests.LogoutRequest;
import common.net.responses.AuthResponse;
import common.net.responses.UserListChangedResponse;
import server.persistence.IPersistence;

public class ServerAdapter extends AbstractServerAdapter {
	private static ServerAdapter instance;
	
	public static ServerAdapter getInstance(IPersistence persistence, ServerDirectory serverDir) {
		if(instance == null) {
			instance = new ServerAdapter(persistence, serverDir);
		}
		return instance;
	}
	
	private ServerAdapter(IPersistence persistence, ServerDirectory serverDir) {
		super(persistence, serverDir);
	}

	@Override
	protected void process(Socket client, AuthRequest obj) {
		User user;
		if (obj.wantsToRegister()) {
			user = persistence.registerUser(obj.getUsername(),
					obj.getPassword());
		} else {
			user = persistence.loginUser(obj.getUsername(),
					obj.getPassword());
		}

		if (user != null) {
			Communication.send(client,
					new AuthResponse(true, serverDir.getActiveUsers()));
			Communication.send(serverDir.getActiveSockets(),
					new UserListChangedResponse(true, user));
			serverDir.idClient(client, user);
		} else {
			Communication.send(client, new AuthResponse(false, null));
		}
	}

	@Override
	protected void process(Socket client, LogoutRequest obj) {
		serverDir.RemoveClient(client);
		// TODO remove open challenges
		// TODO inform other clients
	}
}
