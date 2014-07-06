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

import com.fasterxml.jackson.databind.ObjectMapper;

public class NetUtils {
	public static final String IP = "127.0.0.1";
	public static final int PORT = 11111;
	private static final int MSG_LENGTH = 1024;
	private static final Logger logger = Logger.getLogger("Net");
	private static final ObjectMapper mapper = new ObjectMapper();

	public static String read(Socket socket) throws RuntimeException {
		char[] buffer = new char[MSG_LENGTH];
		int charCount = -1;

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			charCount = bufferedReader.read(buffer, 0, MSG_LENGTH);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error reading message", e);
		}

		if (charCount < 0) {
			throw new RuntimeException("end of input stream");
		}

		return new String(buffer, 0, charCount);
	}

	public static boolean send(Socket client, Object obj)
			throws RuntimeException {
		try {
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
					client.getOutputStream()));
			String json = mapper.writeValueAsString(obj);

			if (json.length() > MSG_LENGTH)
				throw new RuntimeException(
						"message too long for server's read-buffer");

			printWriter.print(json);
			printWriter.flush();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "error sending message", e);
			return false;
		}

		return true;
	}

	public static void send(Collection<Socket> clients, Object obj) {
		for (Socket client : clients) {
			send(client, obj);
		}
	}

	public static <T> T fromJson(String json, Class<T> classOf) {
		try {
			return mapper.readValue(json, classOf);
		} catch (Exception e) {
			return null;
		}
	}
}
