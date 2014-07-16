package common.net;

public class SocketReadException extends Exception {
	private static final long serialVersionUID = 1429370800355851626L;
	private final boolean socketClosed;
	
	public SocketReadException(boolean socketClosed, String string) {
		super(string);
		this.socketClosed = socketClosed;
	}
	
	public SocketReadException(boolean socketClosed, Throwable e) {
		super(e);
		this.socketClosed = socketClosed;
	}

	public boolean isSocketClosed() {
		return socketClosed;
	}
}
