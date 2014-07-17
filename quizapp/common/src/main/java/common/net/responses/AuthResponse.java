package common.net.responses;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

/**
 * Response to inform the user whether the registration and/or login was
 * successful. In case of success, a collection of other currently online
 * users will be provided.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class AuthResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 3090285480950044743L;
	/**
	 * True if the he registration and/or login was successful.
	 */
	private boolean success;
	/**
	 * Collection of other currently online users.
	 */
	private Collection<User> users;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public AuthResponse() {
	}
	
	/**
	 * Creates an instance.
	 * @param success true if the he registration and/or login was successful
	 * @param users collection of other currently online users
	 */
	public AuthResponse(boolean success, Collection<User> users) {
		this.success = success;
		this.users = users;
	}
	
	// === getters ===
	public boolean isSuccess() {
		return success;
	}

	public Collection<User> getUsers() {
		return users;
	}
}
