package common.entities;

import common.entities.annotations.NotPersisted;

/**
 * This class represents a registered user in the system.
 * 
 * @author Tim Wiechers
 */
public class User {
	/**
	 * ID for unique identification.
	 */
	private int id;
	/**
	 * True if admin.
	 */
	private boolean isAdmin;
	/**
	 * Username.
	 */
	private String name;
	/**
	 * The amount of matches the user played.
	 */
	@NotPersisted
	private int matchCount;
	/**
	 * The amount of matches the user won.
	 */
	@NotPersisted
	private int winCount;
	/**
	 * The amount of points the user scored in all of his games.
	 */
	@NotPersisted
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
	 * @param isAdmin
	 *            true if admin
	 */
	public User(String name, boolean isAdmin) {
		this.name = name;
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return String.format(
				"User[%s, id: %d, matches: %d, wins: %d, points: %d]", name,
				id, matchCount, winCount, pointCount);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof User))
			return false;
		User otherUser = (User) other;
		return this.id == otherUser.id && this.name.equals(otherUser.name)
				&& this.matchCount == otherUser.matchCount
				&& this.winCount == otherUser.matchCount
				&& this.pointCount == otherUser.pointCount
				&& this.isAdmin == otherUser.isAdmin;
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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
