package common.net.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.Match;
import common.entities.User;

/**
 * Response to notify a client that a match was created. Includes a list of IDs
 * of the questions that will be posed.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchCreatedResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -8468892503273560460L;
	/**
	 * Match.
	 */
	private Match match;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public MatchCreatedResponse() {
	}

	/**
	 * Creates an instance.
	 */
	public MatchCreatedResponse(Match match) {
		this.match = match;
	}

	// === getters ===
	
	public Match getMatch() {
		return match;
	}
}
