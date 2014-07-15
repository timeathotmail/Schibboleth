package common.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

public class Config {
	/**
	 * Name of property files.
	 */
	private static final String FILE = "config.properties";
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Config");
	/**
	 * Loaded properties.
	 */
	private final Properties prop = new Properties();

	/**
	 * Tries to load a property file in the current working directory.
	 * 
	 * @return a Config instance
	 * @throws ConfigurationException
	 */
	public static Config get() throws ConfigurationException {
		return new Config();
	}

	/**
	 * Creates an instance.
	 * 
	 * @throws ConfigurationException
	 */
	private Config() throws ConfigurationException {
		InputStream input = null;

		try {
			input = new FileInputStream(FILE);
			prop.load(input);
		} catch (IOException e) {
			throw new ConfigurationException(String.format(
					"No %s file in current working dir!", FILE));
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.log(Level.INFO, "Couldn't close FileInputStream", e);
				}
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws ConfigurationException
	 */
	public String get(String key) throws ConfigurationException {
		String result = prop.getProperty(key);

		if (result == null) {
			throw new ConfigurationException(String.format(
					"Key %s is missing!", key));
		}
		return prop.getProperty(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws ConfigurationException
	 */
	public int getInt(String key) throws ConfigurationException {
		int result;

		try {
			result = Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			throw new ConfigurationException(String.format(
					"Key %s was expected to be a number!", key));
		}
		return result;
	}
}
