package common.net.responses;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeDeniedResponse {
	/**
	 * The user.
	 */
	private User user;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeDeniedResponse() {
	}

	public ChallengeDeniedResponse(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
