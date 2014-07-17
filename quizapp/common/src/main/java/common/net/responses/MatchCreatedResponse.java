package common.net.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

/**
 * Response to notify a client that a match was created. Includes a list of IDs
 * of the questions that will be posed.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchCreatedResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -8468892503273560460L;
	/**
	 * Opponent.
	 */
	private User user;
	/**
	 * IDs of questions that will be posed.
	 */
	private List<Integer> questionIds;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public MatchCreatedResponse() {
	}

	/**
	 * Creates an instance.
	 * @param user the opponent
	 * @param questionIds IDs of questions that will be posed
	 */
	public MatchCreatedResponse(User user, List<Integer> questionIds) {
		this.questionIds = questionIds;
		this.user = user;
	}

	// === getters ===
	
	public List<Integer> getQuestionIds() {
		return questionIds;
	}

	public User getUser() {
		return user;
	}
}
