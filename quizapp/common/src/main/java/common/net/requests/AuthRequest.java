package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class AuthRequest {

	private String username;
	private String password;
	private boolean register;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean wantsToRegister() {
		return register;
	}

	@Deprecated
	public AuthRequest() {
	}

	public AuthRequest(String username, String password, boolean register) {
		this.username = username;
		this.password = password;
		this.register = register;
	}
}