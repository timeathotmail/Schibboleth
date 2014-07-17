package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to deny a challenge.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeDenyRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 2219536494308556535L;
	/**
	 * The opponents's nickname.
	 */
	private String opponent;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeDenyRequest() {
	}

	/**
	 * Creates an instance.
	 * @param opponent the opponents's nickname
	 */
	public ChallengeDenyRequest(String opponent) {
		this.opponent = opponent;
	}
	
	// === getters ===
	
	public String getOpponent() {
		return opponent;
	}
}