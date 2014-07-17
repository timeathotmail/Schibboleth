package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to accept a challenge.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeAcceptRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 7851926725938339285L;
	/**
	 * The opponents's nickname.
	 */
	private String opponent;
	
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
	public ChallengeAcceptRequest(String opponent) {
		this.opponent = opponent;
	}
	
	// === getters ===
	
	public String getOpponent() {
		return opponent;
	}
}