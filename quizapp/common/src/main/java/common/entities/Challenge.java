package common.entities;

import java.io.Serializable;

/**
 * This class represents a challenge sent from one user to another.
 * 
 * @author Tim Wiechers
 */
public class Challenge implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 2628023559403665091L;
	/**
	 * ID for unique identification.
	 */
	private int rowid;
	/**
	 * User that sent the challenge.
	 */
	private User from;
	/**
	 * rowid of the user that sent the challenge.
	 */
	private int fromId;
	/**
	 * User that received the challenge.
	 */
	private User to;
	/**
	 * rowid of the user that received the challenge.
	 */
	private int toId;
	
	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public Challenge() {
	}
	
	/**
	 * Creates an instance.
	 * @param from the user that sent the challenge
	 * @param to the user that received the challenge
	 */
	public Challenge(User from, User to) {
		setFrom(from);
		setTo(to);
	}
	
	// === getters & setters ===

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

	// === special methods ===
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Challenge [rowid=" + rowid + ", from=" + from + ", fromId="
				+ fromId + ", to=" + to + ", toId=" + toId + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + fromId;
		result = prime * result + rowid;
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + toId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Challenge other = (Challenge) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (fromId != other.fromId)
			return false;
		if (rowid != other.rowid)
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (toId != other.toId)
			return false;
		return true;
	}
}
