package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

/**
 * Response to inform clients that a new user connected or a user has left.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class UserListChangedResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -2285567679731596648L;
	/**
	 * True if the user connected. False if the user disconnected.
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
	 * 
	 * @param connected
	 *            true if the user connected
	 * @param user
	 *            the user
	 */
	public UserListChangedResponse(boolean connected, User user) {
		this.connected = connected;
		this.user = user;
	}

	// === getters ===
	public boolean isConnected() {
		return connected;
	}

	public User getUser() {
		return user;
	}
}
