package common.entities;

import java.io.Serializable;

/**
 * This class represents a registered user in the system.
 * 
 * @author Tim Wiechers
 */
public class User implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -8493063180883131770L;
	/**
	 * ID for unique identification.
	 */
	private int rowid;
	/**
	 * True if admin.
	 */
	private boolean isAdmin;
	/**
	 * Username.
	 */
	private String name;

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

	public int getRowId() {
		return rowid;
	}

	public void setRowId(int id) {
		this.rowid = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	// === special methods ===

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [rowid=" + rowid + ", isAdmin=" + isAdmin + ", name="
				+ name + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rowid;
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		User other = (User) obj;
		if (rowid != other.rowid)
			return false;
		if (isAdmin != other.isAdmin)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
