/**
 * @author Zaen Khilji
 */
package net.varago;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.UIManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.varago.applet.AppletViewer;

// TODO: Auto-generated Javadoc
/**
 * The Class Launcher.
 */
public class Launcher {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String... args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading system look");
		}
		Gson gson = new GsonBuilder().setLenient().create();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Settings.DATA)))) {
			JsonObject obj = gson.fromJson(reader, JsonObject.class);
			Settings.SECRET = obj.get("secret").getAsString();
			System.out.println(encryptSHA1(obj.get("secret").getAsString(), obj.get("keyString").getAsString()));
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading properties");
		}
		try {
			AppletViewer appletViewer = new AppletViewer();
			// appletViewer.start();
		} catch (Throwable t) {
			t.printStackTrace();
			System.err.println("Error loading applet");
		}
	}

	private static String encryptSHA1(String secret, String keyString)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(key);

		byte[] bytes = mac.doFinal(secret.getBytes("UTF-8"));
		return new String(Base64.encode(bytes));
	}
}