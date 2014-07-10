package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerProperties {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("ServerProperties");
	private static final Properties prop = new Properties();

	static {
		InputStream input = null;

		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Couldn't read .properties file", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Couldn't close FileInputStream",
							e);
				}
			}
		}
	}
	
	public static String get(String key) {
		return prop.getProperty(key);
	}
}
