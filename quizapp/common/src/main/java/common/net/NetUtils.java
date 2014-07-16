package common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		if(instance == null) {
			instance = new NetUtils();
		}
		return instance;
	}
	
	/**
	 * Creates an instance.
	 * @throws ConfigurationException
	 */
	private NetUtils() throws ConfigurationException {
		MSG_LENGTH = Config.get().getInt("MSG_LENGTH");
	}
	
	/**
	 * Waits for an incoming message and returns it.
	 * 
	 * @param socket
	 *            the monitored socket
	 * @return the received message
	 * @throws RuntimeException
	 *             in case the socket was closed
	 */
	public String read(Socket socket) throws SocketReadException {
		if(socket == null) {
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
	 * Receives an object, serializes it as JSON and sends it to a socket.
	 * 
	 * @param socket
	 *            the socket to receive the message
	 * @param obj
	 *            the object to send
	 * @return true if the message was sent
	 * @throws RuntimeException
	 *             in case the message was too long
	 */
	public boolean send(Socket socket, Object obj) {
		if(socket == null || obj == null) {
			return false;
		}
		
		try {
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			String json = mapper.writeValueAsString(obj);

			if (json.length() > MSG_LENGTH) // TODO andere exception?
				throw new RuntimeException(
						"message too long for server's read-buffer ("
								+ MSG_LENGTH + ")");

			printWriter.print(json);
			printWriter.flush();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "error sending message", e);
			return false;
		}

		return true;
	}

	/**
	 * Sends an object to multiple sockets.
	 * 
	 * @param sockets
	 *            the receiving sockets
	 * @param obj
	 *            the object to send
	 */
	public void send(Collection<Socket> sockets, Object obj) {
		for (Socket socket : sockets) {
			send(socket, obj);
		}
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
	public <T> T fromJson(String json, Class<T> classOf) {
		try {
			return mapper.readValue(json, classOf);
		} catch (IllegalArgumentException e) {
			// doesn't match with provided class
		} catch (Exception e) {
			logger.log(Level.SEVERE, "unexpected error parsing json", e);
		}

		return null;
	}
}
