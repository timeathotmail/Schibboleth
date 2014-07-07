package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to change a client's user data.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class UserDataChangeRequest {
	/**
	 * The user's desired new nickname.
	 */
	private String newUsername;
	/**
	 * The user's desired new password.
	 */
	private String newPassword;
	/**
	 * The user's desired new password again to confirm.
	 */
	private String newPasswordConfirm;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public UserDataChangeRequest() {
	}

	/**
	 * Creates an instance.
	 * @param username the user's name
	 * @param password the user's password
	 * @param register true if the user wants to register
	 */
	public UserDataChangeRequest(String newUsername, String newPassword, String newPasswordConfirm) {
		this.newUsername = newUsername;
		this.newPassword = newPassword;
		this.newPasswordConfirm = newPasswordConfirm;
	}
	
	// === getters ===
	public String getNewUsername() {
		return newUsername;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}
}