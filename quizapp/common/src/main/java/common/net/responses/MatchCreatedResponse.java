package common.net.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchCreatedResponse {
	private User user;
	private List<Integer> questionIds;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public MatchCreatedResponse() {
	}

	public MatchCreatedResponse(User user, List<Integer> questionIds) {
		this.questionIds = questionIds;
		this.user = user;
	}

	public List<Integer> getQuestionIds() {
		return questionIds;
	}

	public User getUser() {
		return user;
	}
}
