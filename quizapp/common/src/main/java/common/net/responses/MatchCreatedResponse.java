package common.net.responses;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchCreatedResponse {
	private boolean sendQuestions;
	private User user;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public MatchCreatedResponse() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param success
	 *            true if the he registration and/or login was successful
	 * @param users
	 *            collection of other currently online users
	 */
	public MatchCreatedResponse(User user, boolean sendQuestions) {
		this.sendQuestions = sendQuestions;
		this.user = user;
	}

	public boolean hasToSendQuestions() {
		return sendQuestions;
	}

	public User getUser() {
		return user;
	}
}
