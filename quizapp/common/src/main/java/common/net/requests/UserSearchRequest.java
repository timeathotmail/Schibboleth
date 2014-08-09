package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request used to search for a user by name.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class UserSearchRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -6028949922523270520L;
	/**
	 * The name to search for.
	 */
	private String name;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public UserSearchRequest() {
	}

	public UserSearchRequest(String name) {
		this.name = name;
	}

	// === getters ===

	public String getName() {
		return name;
	}
}
