package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.User;

/**
 * Response to send the client the result of a user search.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class UserSearchResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -6028949922523270520L;
	/**
	 * The found user.
	 */
	private User user;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public UserSearchResponse() {
	}

	public UserSearchResponse(User user) {
		this.user = user;
	}

	// === getters ===

	public User getUser() {
		return user;
	}
}
