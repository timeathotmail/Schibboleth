package common.net.responses;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
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
