package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to send a challenge to a client.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeAcceptRequest {
	/**
	 * The opponents's nickname.
	 */
	private String opponent;
	/**
	 * The first question's id.
	 */
	private int questionId;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeAcceptRequest() {
	}

	/**
	 * Creates an instance.
	 * @param opponent the opponents's nickname
	 */
	public ChallengeAcceptRequest(String opponent, int questionId) {
		this.opponent = opponent;
		this.questionId = questionId;
	}
	
	// === getters ===
	
	public String getOpponent() {
		return opponent;
	}
	
	public int getQuestionId() {
		return questionId;
	}
}