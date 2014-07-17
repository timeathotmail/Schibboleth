package common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

/**
 * This class loads relevant property files and provides access to the data.
 * 
 * @author Tim Wiechers
 */
public class Config {
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger("Config");
	/**
	 * Name of property files.
	 */
	private static final String FILE = "config.properties";
	/**
	 * Singleton instance.
	 */
	private static Config instance;
	/**
	 * Loaded properties.
	 */
	private final Properties prop = new Properties();

	/**
	 * Loads the common properties and, depending on the working directory,
	 * server or client properties.
	 * 
	 * @return a Config instance
	 * @throws ConfigurationException
	 *             when the property files are not found or cannot be read
	 */
	public static Config get() throws ConfigurationException {

		if (instance == null) {
			File f = new File(System.getProperty("user.dir")); // working dir

			instance = new Config();
			instance.load(f.getParent() + File.separator + "common"); // common

			if (f.getName().equals("server")) {
				instance.load(f.getAbsolutePath()); // server
			} else {
				instance.load(f.getParent() + File.separator + "core"); // client
			}
		}

		return instance;
	}

	/**
	 * Loads the content of a properties file.
	 * 
	 * @param dir
	 *            directory that contains the file
	 * @throws ConfigurationException
	 *             when the file cannot be found or read
	 */
	private void load(String dir) throws ConfigurationException {
		InputStream input;
		try {
			input = new FileInputStream(dir + File.separator + FILE);
		} catch (FileNotFoundException e1) {
			throw new ConfigurationException("No properties file in " + dir);
		}

		try {
			instance.prop.load(input);
		} catch (IOException e) {
			throw new ConfigurationException("Error reading from " + dir
					+ File.separator + FILE);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.log(Level.INFO, "Couldn't close FileInputStream", e);
			}
		}
	}

	/**
	 * Returns the String value of the property with the given key.
	 * 
	 * @param key
	 *            the property key
	 * @return property value
	 * @throws ConfigurationException
	 *             when the key doesn't exist
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
	 * Returns the int value of the property with the given key.
	 * 
	 * @param key
	 *            the property key
	 * @return property value
	 * @throws ConfigurationException
	 *             when the key doesn't exist or the value couldn't be parsed to
	 *             int
	 */
	public int getInt(String key) throws ConfigurationException {
		try {
			return Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			throw new ConfigurationException(String.format(
					"Key %s was expected to be a number!", key));
		}
	}
}
