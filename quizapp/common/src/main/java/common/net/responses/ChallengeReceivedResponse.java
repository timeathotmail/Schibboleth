package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.Challenge;

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
	 * The received challenge.
	 */
	private Challenge challenge;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeReceivedResponse() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param challenge
	 *            the received challenge
	 */
	public ChallengeReceivedResponse(Challenge challenge) {
		this.challenge = challenge;
	}

	// === getters ===

	public Challenge getChallenge() {
		return challenge;
	}
}
