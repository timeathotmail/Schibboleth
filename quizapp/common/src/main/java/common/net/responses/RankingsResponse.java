package common.net.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

/**
 * Response to send a client a section of the ranking.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class RankingsResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -7310386520949241908L;
	/**
	 * List of ranked users.
	 */
	private List<User> users;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public RankingsResponse() {
	}

	/**
	 * Creates an instance.
	 * @param users list of ranked users
	 */
	public RankingsResponse(List<User> users) {
		this.users = users;
	}

	// === getters ===
	
	public List<User> getUsers() {
		return users;
	}
}
