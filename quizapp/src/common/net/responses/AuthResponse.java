package common.net.responses;
import java.util.Collection;

import common.entities.User;

public class AuthResponse {

	private boolean success;
	private Collection<User> users;
	
	public AuthResponse(boolean success, Collection<User> users) {
		this.success = success;
		this.users = users;
	}
	
	@Deprecated
	public AuthResponse() {
	}

	public boolean isSuccess() {
		return success;
	}
	
	public Collection<User> getUsers() {
		return users;
	}
}
