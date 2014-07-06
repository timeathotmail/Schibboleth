package common.net.responses;

import common.entities.User;

public class UserListChangedResponse {
	private boolean entered;
	private User user;
	
	@Deprecated
	public UserListChangedResponse() {
	}
	
	public UserListChangedResponse(boolean entered, User user) {
		this.entered = entered;
		this.user = user;
	}
	
	public boolean hasEntered() {
		return entered;
	}
	
	public User getUser() {
		return user;
	}
}
