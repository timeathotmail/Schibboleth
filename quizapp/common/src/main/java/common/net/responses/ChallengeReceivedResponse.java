package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

/**
 * Response to notify a client that she/he received a challenge.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeReceivedResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 5457660819125333124L;
	/**
	 * The sending user.
	 */
	private User user;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeReceivedResponse() {
	}

	/**
	 * Creates an instance
	 * @param user the sending user
	 */
	public ChallengeReceivedResponse(User user) {
		this.user = user;
	}

	// === getters ===
	
	public User getUser() {
		return user;
	}
}
