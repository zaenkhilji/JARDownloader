/**
 * @author Zaen Khilji
 */
package net.varago;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

// TODO: Auto-generated Javadoc
/**
 * The Class Settings.
 */
public class Settings {

	/** The Constant PORTUGESE. */
	public static final int ENGLISH = 0, GERMAN = 1, FRENCH = 2, PORTUGESE = 3;

	/** The Constant NAME. */
	public static final String NAME = "Varago";

	/** The Constant APPLICATION_NAME. */
	public static final String APPLICATION_NAME = "applet_stuber.jar";

	/** The Constant URL. */
	public static final String URL = "http://162.212.253.55/";

	/** The Constant STUB_URL. */
	public static final String STUB_URL = URL + "assets/java/" + APPLICATION_NAME;

	/** The Constant CHECKSUM_URL. */
	public static final String HASH_URL = URL + "assets/json/hashValidator.json";

	/** The Constant JAVA_HOME. */
	public static final File JAVA_HOME = new File(System.getProperty("java.home") + File.separator + "bin");

	/** The Constant DATA. */
	public static final String DATA = "./properties.json";

	public static String SECRET = null;

	/**
	 * Check configurtion.
	 *
	 * @throws Throwable
	 *             the throwable
	 */
	public static void checkConfigurtion() throws Throwable {
		Path config = Paths.get(getStorageDirectory(), new String[0]);
		if (!Files.exists(config, new LinkOption[0]))
			Files.createDirectory(config, new FileAttribute[0]);
	}

	/**
	 * Gets the store directory.
	 *
	 * @return the store directory
	 */
	public static String getStorageDirectory() {
		String userHome = System.getProperty("user.home");
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			return userHome + "\\." + NAME.toLowerCase() + "_storage\\";
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			return userHome + "//." + NAME.toLowerCase() + "_storage//";
		throw new RuntimeException("Could not store local files.");
	}

	/**
	 * Open stream.
	 *
	 * @param urlAsString
	 *            the url as string
	 * @return the input stream
	 * @throws Throwable
	 *             the throwable
	 */
	public static InputStream openStream(String urlAsString) throws Throwable {
		URL url = new URL(urlAsString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");

		return connection.getInputStream();
	}
}