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
	/**
	 * The challenge to deny.
	 */
	private Challenge challenge;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeDenyRequest() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param challenge
	 *            the challenge to deny
	 */
	public ChallengeDenyRequest(Challenge challenge) {
		this.challenge = challenge;
	}

	// === getters ===

	public Challenge getChallenge() {
		return challenge;
	}
}