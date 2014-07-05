package common.entities;

public class User {
	public enum Role {
		Player, Admin
	}

	private int id;
	private String name;
	private Role role;
	private int matchCount;
	private int winCount;
	private int pointCount;

	@Deprecated
	public User() {
	}

	public User(String name, Role role) {
		this.name = name;
		this.role = role;
	}

	public User(String name, Role role, int matchCount, int winCount,
			int pointCount) {
		this.name = name;
		this.role = role;
		this.matchCount = matchCount;
		this.winCount = winCount;
		this.pointCount = pointCount;
	}

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
