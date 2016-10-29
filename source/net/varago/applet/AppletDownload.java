/**
 * @author Zaen Khilji
 */
package net.varago.applet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

import net.varago.Settings;
import net.varago.Updater;

// TODO: Auto-generated Javadoc
/**
 * The Class AppletDownload.
 */
public class AppletDownload extends Observable implements Runnable {

	/** The url. */
	private URL url;

	/** The applet viewer. */
	private final AppletViewer appletViewer;

	/** The updater. */
	private final Updater updater;

	/** The Constant MAX_BUFFER_SIZE. */
	private static final int MAX_BUFFER_SIZE = 1024;

	/** The Constant STATUSES. */
	public static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled", "Error" };

	/** The Constant ERROR. */
	public static final int DOWNLOADING = 0, PAUSED = 1, COMPLETE = 2, CANCELLED = 3, ERROR = 4;

	/** The status. */
	private int size, downloaded, status;

	/**
	 * Instantiates a new applet download.
	 *
	 * @param url            the url
	 * @param appletViewer            the applet viewer
	 * @param updater the updater
	 */
	public AppletDownload(URL url, AppletViewer appletViewer, Updater updater) {
		this.url = url;
		this.appletViewer = appletViewer;
		this.updater = updater;
		size = -1;
		downloaded = 0;
		status = DOWNLOADING;

		this.appletViewer.updateMessage("Downloading gamepack");
		this.appletViewer.updatePercentage(Math.round(this.getProgress()));
		startDownload();
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getURL() {
		return url.toString();
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the progress.
	 *
	 * @return the progress
	 */
	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the pause.
	 */
	public void setPause() {
		status = PAUSED;
		stateChanged();
	}

	/**
	 * Sets the resume.
	 */
	public void setResume() {
		status = DOWNLOADING;
		stateChanged();
		startDownload();
	}

	/**
	 * Sets the cancel.
	 */
	public void setCancel() {
		status = CANCELLED;
		stateChanged();
	}

	/**
	 * Sets the error.
	 */
	private void setError() {
		status = ERROR;
		stateChanged();
	}

	/**
	 * Start download.
	 */
	private void startDownload() {
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Gets the file.
	 *
	 * @param url
	 *            the url
	 * @return the file
	 */
	private String getFile(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// connection.setRequestProperty("Range", "bytes=" + downloaded +
			// "-");
			connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

			if (connection.getResponseCode() / 100 != 2)
				setError();

			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				setError();
			}
			if (size == -1) {
				size = contentLength;
				stateChanged();
			}

			file = new RandomAccessFile(getFile(url), "rw");
			file.seek(downloaded);

			stream = connection.getInputStream();
			while (status == DOWNLOADING) {
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE)
					buffer = new byte[MAX_BUFFER_SIZE];
				else
					buffer = new byte[size - downloaded];

				int read = stream.read(buffer);
				if (read == -1)
					break;

				file.write(buffer, 0, read);
				downloaded += read;
				stateChanged();
			}
			if (status == DOWNLOADING) {
				status = COMPLETE;
				stateChanged();
			}
			if (status == COMPLETE) {
				File appletA = new File(Settings.APPLICATION_NAME);
				File appletB = new File(Settings.getStorageDirectory() + Settings.APPLICATION_NAME);

				inputStream = new FileInputStream(appletA);
				outputStream = new FileOutputStream(appletB);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
					this.appletViewer.updateMessage("Copying new files (" + length + ")");
				}

				inputStream.close();
				outputStream.close();

				this.appletViewer.updateMessage("Deleting old files");
				appletA.delete();
				try {
					File applet = new File(Settings.getStorageDirectory() + Settings.APPLICATION_NAME);
					updater.load(applet);
				} catch (Throwable e) {
					this.appletViewer.updateMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			setError();
			this.appletViewer.updateMessage(e.getMessage());
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
					this.appletViewer.updateMessage(e.getMessage());
					e.printStackTrace();
				}
			}

			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					this.appletViewer.updateMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * State changed.
	 */
	private void stateChanged() {
		setChanged();
		notifyObservers();
		this.appletViewer.updatePercentage(Math.round(this.getProgress()));
	}
}