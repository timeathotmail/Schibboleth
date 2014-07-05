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

public class Communication {
	private static final Logger logger = Logger.getLogger("Communication");

	public static String read(Socket socket) throws RuntimeException {
		char[] buffer = new char[1024];
		int charCount = -1;

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			charCount = bufferedReader.read(buffer, 0, 1024);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "error reading message", e);
		}

		if (charCount < 0) {
			throw new RuntimeException("end of input stream");
		}

		return new String(buffer, 0, charCount);
	}

	public static boolean send(Socket client, Object obj) {
		try {
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
					client.getOutputStream()));
			printWriter.print(new ObjectMapper().writeValueAsString(obj));
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
}
