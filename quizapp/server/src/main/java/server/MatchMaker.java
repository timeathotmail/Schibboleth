package server;

import java.net.Socket;

import common.entities.User;
import common.net.NetUtils;
import common.net.responses.MatchCreatedResponse;
import common.net.responses.MatchSearchCancelledResponse;

public class MatchMaker {
	private Socket waitingClient;
	private User waitingUser;
	private int waitingRevision;

	public synchronized void addUserToSearch(Socket client, User user,
			int revision) {
		if (waitingUser == null) {
			// wait
			waitingUser = user;
			waitingClient = client;
			waitingRevision = revision;
		} else {
			// assert that both clients have the question locally avaiable
			boolean sendQuestions = waitingRevision <= revision;

			// connect
			NetUtils.send(waitingClient, new MatchCreatedResponse(user,
					sendQuestions));
			NetUtils.send(client, new MatchCreatedResponse(waitingUser,
					!sendQuestions));

			// clear spot
			waitingUser = null;
		}
	}

	public synchronized void removeUserFromSearch() {
		NetUtils.send(waitingClient, new MatchSearchCancelledResponse());
		waitingClient = null;
	}
}
