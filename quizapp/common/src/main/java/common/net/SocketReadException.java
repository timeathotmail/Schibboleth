package common.net;

/**
 * Thrown to indicate that an error occured while receiving from a socket.
 * 
 * @author Tim Wiechers
 */
public class SocketReadException extends Exception {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 1429370800355851626L;
	/**
	 * True if the socket was closed.
	 */
	private final boolean socketClosed;

	/**
	 * Creates an instance.
	 * 
	 * @param socketClosed
	 *            true if the socket was closed
	 * @param string
	 *            error message
	 */
	public SocketReadException(boolean socketClosed, String string) {
		super(string);
		this.socketClosed = socketClosed;
	}

	/**
	 * Creates an instance.
	 * 
	 * @param socketClosed
	 *            true if the socket was closed
	 * @param e
	 *            cause
	 */
	public SocketReadException(boolean socketClosed, Throwable e) {
		super(e);
		this.socketClosed = socketClosed;
	}

	/**
	 * @return True if the socket was closed
	 */
	public boolean isSocketClosed() {
		return socketClosed;
	}
}
