/**
 * @author Zaen Khilji
 */
package net.varago;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.UIManager;

import net.varago.applet.AppletViewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

// TODO: Auto-generated Javadoc
/**
 * The Class Launcher.
 */
public class Launcher {

	private static BufferedReader webReader, localReader;

	private static JsonObject webJsonObject, localJsonObject;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException {
		Gson gson = new GsonBuilder().setLenient().create();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading system look");
		}
		try {
			webReader = new BufferedReader(new InputStreamReader(new URL(
					Settings.HASH_URL).openStream()));
			webJsonObject = gson.fromJson(webReader, JsonObject.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			localReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(Settings.DATA)));
			localJsonObject = gson.fromJson(localReader, JsonObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading properties");
		}
		try {
			AppletViewer appletViewer = new AppletViewer();
			if (webJsonObject.get("secret").getAsString() == localJsonObject
					.get("secret").getAsString())
				appletViewer.start();
		} catch (Throwable t) {
			t.printStackTrace();
			System.err.println("Error loading applet");
		}
		try {
			String SHA1 = encryptSHA1(
					webJsonObject.get("secret").getAsString(), webJsonObject.get("keyString").getAsString());
			if (SHA1 == localJsonObject.get("secret").getAsString()) {
				System.out.println("The secrets match!");
			} else
				System.err.println("The secrets does not match: " + SHA1
						+ ", local: "
						+ localJsonObject.get("secret").getAsString());
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		localReader.close();
	}

	private static String encryptSHA1(String secret, String keyString)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException {
		SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"),
				"HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(key);

		byte[] bytes = mac.doFinal(secret.getBytes("UTF-8"));
		return new String(Base64.encode(bytes));
	}
}