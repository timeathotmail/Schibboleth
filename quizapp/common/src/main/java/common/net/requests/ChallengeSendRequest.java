package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.Challenge;

/**
 * Request to send a challenge to a client.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ChallengeSendRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -6294339656210902905L;
	/**
	 * The challenge to send.
	 */
	private Challenge challenge;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ChallengeSendRequest() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param challenge
	 *            the challenge to send
	 */
	public ChallengeSendRequest(Challenge challenge) {
		this.challenge = challenge;
	}

	// === getters ===

	public Challenge getChallenge() {
		return challenge;
	}
}