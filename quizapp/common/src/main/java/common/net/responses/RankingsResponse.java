package common.net.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class RankingsResponse {

	private List<User> users;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public RankingsResponse() {
	}

	public RankingsResponse(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}
}
