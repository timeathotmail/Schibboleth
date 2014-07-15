package common.entities;

import common.entities.annotations.*;

/**
 * This class represents a finished match between two users.
 * 
 * @author Tim Wiechers
 */
@TableAlias(table = "MATCH__")
public class Match {
	/**
	 * ID for unique identification.
	 */
	private int id;
	/**
	 * First player.
	 */
	@NotPersisted
	private User user1;
	/**
	 * Second player.
	 */
	@NotPersisted
	private User user2;
	/**
	 * First player's id.
	 */
	@ColumnAlias(column = "user1")
	private int userId1;
	/**
	 * Second player's id.
	 */
	@ColumnAlias(column = "user2")
	private int userId2;
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
	 * 
	 * @param user1
	 *            first player
	 * @param user2
	 *            second player
	 */
	public Match(User user1, User user2) {
		setUser1(user1);
		setUser2(user2);
	}

	/**
	 * Creates an instance.
	 * 
	 * @param user1
	 *            first player
	 * @param user2
	 *            second player
	 * @param points1
	 *            first player's points
	 * @param points2
	 *            second player's points
	 */
	public Match(User user1, User user2, int points1, int points2) {
		setUser1(user1);
		setUser2(user2);
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

		if (user1 != null) {
			this.userId1 = user1.getId();
		}
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;

		if (user2 != null) {
			this.userId2 = user2.getId();
		}
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

	// === special methods ===

	@Override
	public String toString() {
		return "Match [id=" + id + ", userId1=" + userId1 + ", userId2="
				+ userId2 + ", points1=" + points1 + ", points2=" + points2
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + points1;
		result = prime * result + points2;
		result = prime * result + ((user1 == null) ? 0 : user1.hashCode());
		result = prime * result + ((user2 == null) ? 0 : user2.hashCode());
		result = prime * result + userId1;
		result = prime * result + userId2;
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
		Match other = (Match) obj;
		if (id != other.id)
			return false;
		if (points1 != other.points1)
			return false;
		if (points2 != other.points2)
			return false;
		if (user1 == null) {
			if (other.user1 != null)
				return false;
		} else if (!user1.equals(other.user1))
			return false;
		if (user2 == null) {
			if (other.user2 != null)
				return false;
		} else if (!user2.equals(other.user2))
			return false;
		if (userId1 != other.userId1)
			return false;
		if (userId2 != other.userId2)
			return false;
		return true;
	}
}
