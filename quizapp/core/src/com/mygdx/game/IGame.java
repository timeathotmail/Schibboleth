package com.mygdx.game;

import java.util.Collection;

import common.entities.*;

public interface IGame {
	void autoLogin();
	void login(String username, String password);
	void logout();
	void changeUserData(String username, String pw1, String pw2);
	void playOffline();
	
	void searchMatch();
	void cancelSearch();
	void displayUserList();
	void sendChallenge(User user);
	void displayChallenges();
	void acceptChallenge(User user);
	void denyChallenge(User user);
	void displayRankings(int offset, int length);
	void displaySettings();
	void displayStatistics();
	
	void submitAnswer(int question);
	void leaveMatch();
	void endMatch();
	
	void onNoConnection();
	void onLogin(boolean success, Collection<User> users);
	void onConnectionLost();
	void onUserListChanged(boolean add, User user);
	void onMatchFound(User user);
	void onChallengeAccepted(User user);
	void onChallengeDenied(User user);
	void onChallengeReceived(User user);
	void onOpponentLeft();
	void onOpponentAnswered(int question);
}
