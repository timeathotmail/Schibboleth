package server;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.persistence.Data;
import server.persistence.IPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.entities.User;
import common.net.Communication;
import common.net.requests.AuthRequest;
import common.net.requests.LogoutRequest;
import common.net.responses.AuthResponse;
import common.net.responses.UserListChangedResponse;

public class ServerInbox implements Runnable {
	private static final Logger logger = Logger.getLogger("ServerInbox");
	private final Socket client;
	private final ServerDirectory serverDir;
	private final IPersistence persistence = Data.getInstance();

	public ServerInbox(final Socket client, ServerDirectory serverDir) {
		this.client = client;
		this.serverDir = serverDir;
	}

	public void run() {
		try {
			while (true) {
				processRequest(client, Communication.read(client));
			}
		} catch (RuntimeException e) {
			serverDir.RemoveClient(client);
		}
	}

	private void processRequest(Socket client, String json) {
		ObjectMapper mapper = new ObjectMapper();

		// === login /registration ===
		try {
			AuthRequest req = mapper.readValue(json, AuthRequest.class);
			User user;
			if (req.wantsToRegister()) {
				user = persistence.registerUser(req.getUsername(),
						req.getPassword());
			} else {
				user = persistence.loginUser(req.getUsername(),
						req.getPassword());
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
			return;
		} catch (Exception e) {
		}

		// === logout ===
		try {
			mapper.readValue(json, LogoutRequest.class);
			serverDir.RemoveClient(client);
			// TODO remove open challenges
			// TODO inform other clients
			return;
		} catch (Exception e) {
		}
		
		// TODO rest

		// === else unknown ===
		logger.log(Level.SEVERE, "unknown request: " + json);
	}

}