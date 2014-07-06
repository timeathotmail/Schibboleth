package common.net.responses;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
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
