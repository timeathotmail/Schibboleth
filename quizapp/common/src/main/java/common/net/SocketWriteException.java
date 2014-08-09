package common.net;

/**
 * Thrown to indicate that an error occured while writing to a socket.
 * 
 * @author Tim Wiechers
 */
public class SocketWriteException extends Exception {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -8634521176467943304L;

	/**
	 * Creates an instance.
	 * 
	 * @param string
	 *            error message
	 */
	public SocketWriteException(String string) {
		super(string);
	}

	/**
	 * Creates an instance.
	 * 
	 * @param e
	 *            cause
	 */
	public SocketWriteException(Throwable e) {
		super(e);
	}
}
