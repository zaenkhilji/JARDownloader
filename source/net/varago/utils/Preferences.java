/**
 * @author Zaen Khilji
 */
package net.varago.utils;

import java.util.Hashtable;

// TODO: Auto-generated Javadoc
/**
 * The Class Preferences.
 */
public class Preferences {

	/** The preferences. */
	private final Hashtable<String, String> preferences = new Hashtable<String, String>();

	/**
	 * Read.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void read() throws java.io.IOException {
	}

	/**
	 * Gets the.
	 *
	 * @param uid the uid
	 * @return the string
	 */
	public String get(String uid) {
		return (String) this.preferences.getOrDefault(uid, "null");
	}
}