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

	// === special methods ===

	@Override
	public String toString() {
		return "User [id=" + id + ", isAdmin=" + isAdmin + ", name=" + name
				+ ", matchCount=" + matchCount + ", winCount=" + winCount
				+ ", pointCount=" + pointCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + matchCount;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + pointCount;
		result = prime * result + winCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		if (isAdmin != other.isAdmin)
			return false;
		if (matchCount != other.matchCount)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pointCount != other.pointCount)
			return false;
		if (winCount != other.winCount)
			return false;
		return true;
	}
}
