package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to send a challenge to a client.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeSendRequest {
	/**
	 * The opponents's nickname.
	 */
	private String opponent;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeSendRequest() {
	}

	/**
	 * Creates an instance.
	 * @param opponent the opponents's nickname
	 */
	public ChallengeSendRequest(String opponent) {
		this.opponent = opponent;
	}
	
	// === getters ===
	
	public String getOpponent() {
		return opponent;
	}
}