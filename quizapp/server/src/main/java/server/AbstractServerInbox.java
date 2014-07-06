package server;

import java.net.Socket;

import common.net.NetUtils;
import common.net.requests.AuthRequest;
import common.net.requests.LogoutRequest;

public abstract class AbstractServerInbox implements Runnable {

	protected final Socket client;
	protected final ServerDirectory serverDir;

	protected AbstractServerInbox(final Socket client, ServerDirectory serverDir) {
		this.client = client;
		this.serverDir = serverDir;
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

	abstract protected void process(Socket client, AuthRequest obj);

	abstract protected void process(Socket client, LogoutRequest obj);
}
