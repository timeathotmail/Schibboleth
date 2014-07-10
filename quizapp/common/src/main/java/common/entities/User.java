package common.entities;

/**
 * This class represents a registered user in the system.
 * 
 * @author Tim Wiechers
 */
public class User {
	/**
	 * Enum for distinction of user rights.
	 */
	public enum Role {
		Player, Admin
	}

	/**
	 * ID for unique identification.
	 */
	private int id;
	/**
	 * Username.
	 */
	private String name;
	/**
	 * The user's role in the system.
	 */
	private Role role;
	/**
	 * The amount of matches the user played.
	 */
	private int matchCount;
	/**
	 * The amount of matches the user won.
	 */
	private int winCount;
	/**
	 * The amount of points the user scored in all of his games.
	 */
	private int pointCount;

	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public User() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param name
	 *            the username
	 * @param role
	 *            the user's role
	 */
	public User(String name, Role role) {
		this.name = name;
		this.role = role;
	}

	// === getters & setters ===

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getPointCount() {
		return pointCount;
	}

	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}
}
