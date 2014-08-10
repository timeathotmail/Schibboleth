package common.net.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import common.entities.Challenge;
import common.entities.Match;
import common.entities.User;

/**
 * Response to inform the user whether the registration and/or login was
 * successful. In case of success, client relevant data will provided as well.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 3090285480950044743L;
	/**
	 * Failed AuthResponse.
	 */
	public static final AuthResponse Fail = new AuthResponse(0, null,
			null, null);
	/**
	 * List of other currently online users.
	 */
	private List<User> users;
	/**
	 * List of running matches of the user.
	 */
	private List<Match> runningMatches;
	/**
	 * List of unanswered challenges of the user.
	 */
	private List<Challenge> challenges;
	/**
	 * The time limit for answering questions.
	 */
	private int timeLimit;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public AuthResponse() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param timeLimit
	 *            the time limit for answering questions
	 * @param users
	 *            collection of other currently online users
	 * @param matches
	 *            list of running matches of the user
	 * @param challenges
	 *            list of unanswered challenges of the user
	 */
	public AuthResponse(int timeLimit, List<User> users,
			List<Match> matches, List<Challenge> challenges) {
		this.timeLimit = timeLimit;
		this.runningMatches = matches;
		this.challenges = challenges;
		this.users = users;
	}

	// === getters ===
	public boolean isSuccess() {
		return users != null;
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
