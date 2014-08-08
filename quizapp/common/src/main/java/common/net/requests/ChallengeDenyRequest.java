package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.Challenge;

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

	private Challenge challenge;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeDenyRequest() {
	}

	public ChallengeDenyRequest(Challenge challenge) {
		this.challenge = challenge;
	}
	
	// === getters ===
	
	public Challenge getChallenge() {
		return challenge;
	}
}