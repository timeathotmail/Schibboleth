package server;

/**
 * Exception raised on errors in the ServerDirectory.
 * 
 * @author Tim Wiechers
 */
public class DirectoryException extends Exception {

	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -6515560130419444269L;

	/**
	 * Creates an instance.
	 * 
	 * @param msg
	 *            error message
	 */
	public DirectoryException(String msg) {
		super(msg);
	}
}
