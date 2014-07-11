package common.entities;

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
}
