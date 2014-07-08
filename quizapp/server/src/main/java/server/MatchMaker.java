package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.persistence.IPersistence;
import common.entities.User;

public class MatchMaker implements Runnable {
	/**
	 * Users searching for a quick match.
	 */
	private final List<User> searchingUsers = new ArrayList<User>();

	@Override
	public void run() {
		while (true) {
			// TODO take 2 users out of the list, connect
		}
	}

	public void addUserToSearch(User user) {
		searchingUsers.add(user);
	}
	
	public void removeUserFromSearch(User user) {
		searchingUsers.remove(user);
	}
}
