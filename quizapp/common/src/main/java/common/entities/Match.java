package common.entities;

/**
 * This class represents a finished match between two users.
 * 
 * @author Tim Wiechers
 */
public class Match {
	/**
	 * ID for unique identification.
	 */
	private int id;
	/**
	 * First player.
	 */
	private User user1;
	/**
	 * Second player.
	 */
	private User user2;
	/**
	 * First player's points.
	 */
	private int points1;
	/**
	 * Second player's points.
	 */
	private int points2;
	
	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public Match() {
	}
	
	/**
	 * Creates an instance.
	 * @param user1 first player
	 * @param user2 second player
	 * @param points1 first player's points
	 * @param points2 second player's points
	 */
	public Match(User user1, User user2, int points1, int points2) {
		this.user1 = user1;
		this.user2 = user2;
		this.points1 = points1;
		this.points2 = points2;
	}
	
	// === getters & setters ===

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public int getPoints1() {
		return points1;
	}

	public void setPoints1(int points1) {
		this.points1 = points1;
	}

	public int getPoints2() {
		return points2;
	}

	public void setPoints2(int points2) {
		this.points2 = points2;
	}
}
