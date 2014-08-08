package common.entities;

public class Joker {
	public enum Type {
		FiftyFifty
	}
	
	private int userId;
	private Type type;
	
	@Deprecated
	public Joker() {
	}
	
	public Joker(int userId, Type type) {
		super();
		this.userId = userId;
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
