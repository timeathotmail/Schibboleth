package common.entities;

public class Match {
	private int id;
	private User user1;
	private User user2;
	private int points1;
	private int points2;
	
	@Deprecated
	public Match() {
	}
	
	public Match(User user1, User user2, int points1, int points2) {
		this.user1 = user1;
		this.user2 = user2;
		this.points1 = points1;
		this.points2 = points2;
	}

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
