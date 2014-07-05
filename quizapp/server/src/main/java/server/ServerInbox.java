package server;

import java.net.Socket;

import server.persistence.Data;
import server.persistence.IPersistence;

import common.net.Communication;

public class ServerInbox implements Runnable {
	private final AbstractServerAdapter adapter;
	private final Socket client;
	private final ServerDirectory serverDir;
	private final IPersistence persistence = Data.getInstance();

	public ServerInbox(final Socket client, ServerDirectory serverDir) {
		this.client = client;
		this.serverDir = serverDir;
		this.adapter = ServerAdapter.getInstance(persistence, serverDir);
	}

	public void run() {
		try {
			while (true) {
				adapter.process(client, Communication.read(client));
			}
		} catch (RuntimeException e) {
			serverDir.RemoveClient(client);
		}
	}
}