package common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.utils.Config;

/**
 * This class contains methods to ease socket communication.
 * 
 * @author Tim Wiechers
 */
public class NetUtils {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Net");
	/**
	 * Maps objects to JSON and vice versa.
	 */
	private static final ObjectMapper mapper = new ObjectMapper();
	/**
	 * Singelton instance.
	 */
	private static NetUtils instance;
	/**
	 * Read-buffer capacity and max. length of a message.
	 */
	private final int MSG_LENGTH;

	/**
	 * @return the NetUtils instance
	 * @throws ConfigurationException
	 */
	public static NetUtils getInstance() throws ConfigurationException {
		if (instance == null) {
			instance = new NetUtils();
		}
		return instance;
	}

	/**
	 * Creates an instance.
	 * 
	 * @throws ConfigurationException
	 *             when the value of {@link #MSG_LENGTH} can't be read from
	 *             properties
	 */
	private NetUtils() throws ConfigurationException {
		MSG_LENGTH = Config.get().getInt("MSG_LENGTH");
	}

	// =====================================================================
	// reading
	// =====================================================================

	/**
	 * Waits for an incoming message and returns it.
	 * 
	 * @param socket
	 *            the monitored socket
	 * @return the received message
	 * @throws SocketReadException
	 *             in case the socket was closed or a message was missed due to
	 *             an IOException
	 */
	public String read(Socket socket) throws SocketReadException {
		if (socket == null) {
			return "";
		}

		char[] buffer = new char[MSG_LENGTH];
		int charCount = -1;

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			charCount = bufferedReader.read(buffer, 0, MSG_LENGTH);
		} catch (IOException e) {
			throw new SocketReadException(false, e);
		}

		if (charCount < 0) {
			throw new SocketReadException(true, "end of input stream");
		}

		return new String(buffer, 0, charCount);
	}

	/**
	 * Tries to map a JSON string to an instance of a class.
	 * 
	 * @param json
	 *            the JSON string to map
	 * @param classOf
	 *            the class to map to
	 * @return the object if successful, else null
	 */
	public <T extends Serializable> T fromJson(String json, Class<T> classOf) {
		try {
			return mapper.readValue(json, classOf);
		} catch (IllegalArgumentException e) {
			// doesn't match with provided class
		} catch (Exception e) {
			logger.log(Level.SEVERE, "unexpected error parsing json", e);
		}

		return null;
	}

	// =====================================================================
	// writing
	// =====================================================================

	/**
	 * Sends a serialized object to a socket.
	 * 
	 * @param socket
	 *            the receiving socket
	 * @param obj
	 *            the object to be sent
	 * @throws SocketWriteException
	 *             if the object couldn't be processed or the sending failed
	 * @throws IllegalArgumentException
	 *             if the socket or object is null
	 */
	public void send(Socket socket, Serializable obj) throws SocketWriteException,
			IllegalArgumentException {
		send(socket, toJson(obj));
	}

	/**
	 * Sends a serialized object to multiple sockets.
	 * 
	 * @param sockets
	 *            the receiving sockets
	 * @param obj
	 *            the object to be sent
	 * @throws SocketWriteException
	 *             if the object couldn't be processed or the sending failed
	 * @throws IllegalArgumentException
	 *             if the object or one of the sockets is null
	 */
	public void send(Collection<Socket> sockets, Serializable obj)
			throws SocketWriteException, IllegalArgumentException {

		String json = toJson(obj);

		for (Socket socket : sockets) {
			send(socket, json);
		}
	}

	/**
	 * Sends a message to a socket.
	 * 
	 * @param socket
	 *            the receiving socket
	 * @param message
	 *            the message to be sent
	 * @throws SocketWriteException
	 *             in case of an IOException while sending
	 * @throws IllegalArgumentException
	 *             if the socket is null or the message is empty
	 */
	private void send(Socket socket, String message)
			throws SocketWriteException, IllegalArgumentException {
		if (socket == null) {
			throw new IllegalArgumentException("null socket");
		} else if (message == null || message.isEmpty()) {
			throw new IllegalArgumentException("null or empty message");
		}

		try {
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			printWriter.print(message);
			printWriter.flush();
		} catch (IOException e) {
			throw new SocketWriteException(e);
		}
	}

	/**
	 * Serializes an object as JSON.
	 * 
	 * @param obj
	 *            the object to be processed
	 * @return the JSON string
	 * @throws SocketWriteException
	 *             if the object couldn't be processed or the resulting string's
	 *             length exceeds the maximum length {@link #MSG_LENGTH}
	 * @throws IllegalArgumentException
	 *             if the object is null
	 */
	private String toJson(Serializable obj) throws SocketWriteException,
			IllegalArgumentException {
		if (obj == null) {
			throw new IllegalArgumentException("null object");
		}

		String json;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new SocketWriteException(e);
		}

		if (json.length() > MSG_LENGTH)
			throw new SocketWriteException(
					"message too long for the read-buffer");

		return json;
	}
}
