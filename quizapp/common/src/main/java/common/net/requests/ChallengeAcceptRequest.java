package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.Challenge;

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
	 * The challenge to accept.
	 */
	private Challenge challenge;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeAcceptRequest() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param challenge
	 *            the challenge to accept
	 */
	public ChallengeAcceptRequest(Challenge challenge) {
		this.challenge = challenge;
	}

	// === getters ===

	public Challenge getChallenge() {
		return challenge;
	}
}