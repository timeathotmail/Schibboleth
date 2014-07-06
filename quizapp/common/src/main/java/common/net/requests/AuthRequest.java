package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to register and/or login to identify the user.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class AuthRequest {
	/**
	 * The user's nickname.
	 */
	private String username;
	/**
	 * The user's password.
	 */
	private String password;
	/**
	 * True if the user wants to register.
	 */
	private boolean register;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public AuthRequest() {
	}

	/**
	 * Creates an instance.
	 * @param username the user's name
	 * @param password the user's password
	 * @param register true if the user wants to register
	 */
	public AuthRequest(String username, String password, boolean register) {
		this.username = username;
		this.password = password;
		this.register = register;
	}
	
	// === getters ===
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean wantsToRegister() {
		return register;
	}
}