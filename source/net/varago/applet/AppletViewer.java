/**
 * @author Zaen Khilji
 */
package net.varago.applet;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.varago.Settings;
import net.varago.Updater;
import net.varago.utils.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class AppletViewer.
 */
public class AppletViewer {

	/** The frame. */
	private final JFrame frame = new JFrame();

	/** The preferences. */
	private final Preferences preferences;

	/** The applet component. */
	private final AppletComponent appletComponent;

	/** The updater. */
	private final Updater updater;

	/**
	 * Instantiates a new applet viewer.
	 *
	 * @throws Throwable
	 *             the throwable
	 */
	public AppletViewer() throws Throwable {
		Settings.checkConfigurtion();
		this.preferences = new Preferences();
		this.appletComponent = new AppletComponent(this);
		this.updater = new Updater(this);
		this.frame.getToolkit().setDynamicLayout(true);
		this.frame.setBackground(Color.BLACK);
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AppletViewer appletViewer = new AppletViewer();
			appletViewer.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Start.
	 *
	 * @throws Throwable
	 *             the throwable
	 */
	private void start() throws Throwable {
		if (this.checkAll()) {
			this.preferences.read();
			this.appletComponent.createDialog();
			this.updater.initialize();
		}
	}

	/**
	 * Check all.
	 *
	 * @return true, if successful
	 */
	private boolean checkAll() {
		double version = Double.parseDouble(System.getProperty("java.version").substring(0, 3));
		if (version < 1.6) {
			JOptionPane.showMessageDialog(null,
					"<html>You have an oudated version of Java: " + version
							+ " - please update your Java at <a href='http://google.com'>www.java.com/download</a>.</html>",
					Settings.NAME, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Update message.
	 *
	 * @param messageKey
	 *            the message key
	 */
	public void updateMessage(String messageKey) {
		this.appletComponent.setMessageAndRepaint(messageKey);
		resetPercentage();
	}

	/**
	 * Exit everything.
	 */
	public void exit() {
		this.appletComponent.getDialog().dispose();
		System.exit(0);
	}

	/**
	 * Update percentage.
	 *
	 * @param percentage
	 *            the percentage
	 */
	public void updatePercentage(int percentage) {
		this.appletComponent.updatePercentageAndRepaint(percentage);
	}

	/**
	 * Sets the percentage.
	 *
	 * @param percentage
	 *            the new percentage
	 */
	public void setPercentage(int percentage) {
		this.appletComponent.setPercentageAndRepaint(percentage);
	}

	/**
	 * Reset percentage.
	 */
	public void resetPercentage() {
		this.appletComponent.resetPercentageAndRepaint();
	}

	/**
	 * Gets the preference.
	 *
	 * @param uid
	 *            the uid
	 * @return the preference
	 */
	public String getPreference(String uid) {
		return this.preferences.get(uid);
	}

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public JFrame getFrame() {
		return this.frame;
	}
}