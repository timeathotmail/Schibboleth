package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.Challenge;
import common.entities.User;

/**
 * Response to notify a client that a challenge of hers/his was denied.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeDeniedResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 4552143065236969334L;
	private Challenge challenge;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeDeniedResponse() {
	}

	public ChallengeDeniedResponse(Challenge challenge) {
		this.challenge = challenge;
	}
	
	// === getters ===
	
	public Challenge getChallenge() {
		return challenge;
	}
}
