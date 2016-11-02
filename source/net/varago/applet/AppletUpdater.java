/**
 * @author Zaen Khilji
 */
package net.varago.applet;

import java.io.File;
import java.net.URL;

import net.varago.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class Updater.
 */
public class AppletUpdater {

	/** The applet viewer. */
	private final AppletViewer appletViewer;

	/**
	 * Instantiates a new updater.
	 *
	 * @param appletViewer
	 *            the applet viewer
	 */
	public AppletUpdater(AppletViewer appletViewer) {
		this.appletViewer = appletViewer;
	}

	/**
	 * Initialize.
	 *
	 * @throws Throwable
	 *             the throwable
	 */
	public void initialize() throws Throwable {
		File applet = new File(Settings.getStorageDirectory() + Settings.APPLICATION_NAME);
		if (!applet.exists()) {
			download();
			return;
		}
		long localHash = getLocalHash(applet);
		long remoteHash = getRemoteHash(Settings.HASH_URL);

		if (localHash != remoteHash) {
			download();
			return;
		}

		load(applet);
	}

	/**
	 * Load.
	 *
	 * @param applet
	 *            the stub
	 * @throws Throwable
	 *             the throwable
	 */
	public void load(File applet) throws Throwable {
		if (applet == null) {
			this.appletViewer.updateMessage("Error while loading gamepack");
			return;
		}
		this.appletViewer.updateMessage("Loading gamepack");
		this.appletViewer.updatePercentage(100);

		ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "java", "-jar", applet.getAbsolutePath() });
		processBuilder.directory(Settings.JAVA_HOME);
		processBuilder.start();

		this.appletViewer.exit();
	}

	/**
	 * Update.
	 *
	 * @throws Throwable
	 *             the throwable
	 */
	private void download() throws Throwable {
		new AppletDownload(new URL(Settings.STUB_URL), this.appletViewer, this);
	}

	/**
	 * Checksum from supplier.
	 *
	 * @param url
	 *            the url
	 * @return the long
	 * @throws Throwable
	 *             the throwable
	 */
	public long getRemoteHash(String url) throws Throwable {
		return 0;
	}

	/**
	 * Calculate hash.
	 *
	 * @param stub
	 *            the stub
	 * @return the long
	 * @throws Throwable
	 *             the throwable
	 */
	public long getLocalHash(File stub) throws Throwable {
		return 0;
	}
}