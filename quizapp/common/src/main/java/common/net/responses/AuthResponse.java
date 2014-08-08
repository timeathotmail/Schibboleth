package common.net.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.Challenge;
import common.entities.Match;
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
	
	public static final AuthResponse Fail = new AuthResponse(false, 0, null, null, null);
	
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
	private List<User> users = new ArrayList<User>();
	
	private List<Match> runningMatches;
	
	private List<Challenge> challenges;
	
	private int timeLimit;

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
	public AuthResponse(boolean success, int timeLimit, Collection<User> users, List<Match> matches, List<Challenge> challenges) {
		this.success = success;
		this.timeLimit = timeLimit;
		this.runningMatches = matches;
		this.challenges = challenges;
		
		for(User user : users) {
			this.users.add(user);
		}
	}
	
	// === getters ===
	public boolean isSuccess() {
		return success;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Match> getRunningMatches() {
		return runningMatches;
	}
	
	public List<Challenge> getChallenges() {
		return challenges;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
}
