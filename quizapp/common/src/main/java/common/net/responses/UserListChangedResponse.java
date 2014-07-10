package common.net.responses;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.entities.User;

/**
 * Response to inform clients that a new user connected or a user has left.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class UserListChangedResponse {
	/**
	 * True if the user connected.
	 * False if the user disconnected.
	 */
	private boolean connected;
	/**
	 * The user.
	 */
	private User user;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public UserListChangedResponse() {
	}

	/**
	 * Creates an instance.
	 * @param connected true if the user connected
	 * @param user the user
	 */
	public UserListChangedResponse(boolean connected, User user) {
		this.connected = connected;
		this.user = user;
	}

	// === getters ===
	public boolean hasConnected() {
		return connected;
	}

	public User getUser() {
		return user;
	}
}