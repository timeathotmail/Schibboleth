package common.entities;

import java.io.Serializable;

public class Challenge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2628023559403665091L;

	private int rowid;
	private User from;
	private int fromId;
	private User to;
	private int toId;
	
	@Deprecated
	public Challenge() {
	}
	
	public Challenge(User from, User to) {
		setFrom(from);
		setTo(to);
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		if(from != null) {
			this.fromId = from.getRowId();
		}
		
		this.from = from;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		if(to != null) {
			this.toId = to.getRowId();
		}
		
		this.to = to;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public int getRowId() {
		return rowid;
	}

	public void setRowId(int id) {
		this.rowid = id;
	}
}
