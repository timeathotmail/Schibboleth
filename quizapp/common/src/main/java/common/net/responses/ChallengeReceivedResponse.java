package common.net.responses;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeReceivedResponse {
	/**
	 * The user.
	 */
	private User user;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeReceivedResponse() {
	}

	public ChallengeReceivedResponse(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
