/**
 * @author Zaen Khilji
 */
package net.varago.applet;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.varago.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class AppletComponent.
 */
public class AppletComponent extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The applet viewer. */
	private final AppletViewer appletViewer;

	/** The dialog. */
	private final Dialog dialog;

	/** The window adapter. */
	private final AppletWindowAdapter windowAdapter = new AppletWindowAdapter();

	/** The font. */
	private final Font font = new Font("Optima Nova", 1, 13);

	/** The font metrics. */
	private final FontMetrics fontMetrics = getFontMetrics(this.font);

	/** The percentage. */
	private int percentage;

	/** The message. */
	private String message = "Loading...";

	/**
	 * Instantiates a new applet component.
	 *
	 * @param appletViewer
	 *            the applet viewer
	 */
	public AppletComponent(AppletViewer appletViewer) {
		this.appletViewer = appletViewer;
		this.dialog = new Dialog(appletViewer.getFrame(), Settings.NAME, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();

		g.setColor(new Color(0, 0, 0, 158));
		g.fillRect(0, 0, width, height);

		g.setColor(new Color(255, 128, 0));
		g.drawRect(65384 + width / 2, height / 2 - 18, 303, 33);
		g.fillRect(65384 + width / 2 + 2, 2 + height / 2 + -18, -3 + 303
				* this.percentage / 100, 30);

		String formattedMessage = this.message + " - " + this.percentage + "%";
		g.setColor(new Color(255, 255, 255, 201));
		g.drawString(formattedMessage,
				(-this.fontMetrics.stringWidth(formattedMessage) + width) / 2,
				4 + height / 2);
	}

	/**
	 * Creates the dialog.
	 */
	public void createDialog() {
		this.appletViewer.updateMessage("Loading config");
		this.dialog.add(this);
		this.dialog.addWindowListener(this.windowAdapter);
		this.dialog.setResizable(false);
		this.dialog.setSize(320, 100);
		this.dialog.setLocationRelativeTo(this.appletViewer.getFrame());
		this.dialog.setAlwaysOnTop(true);
		this.dialog.setVisible(true);
		this.appletViewer.updatePercentage(100);
	}

	/**
	 * Update percentage and repaint.
	 *
	 * @param percentage
	 *            the percentage
	 */
	public void updatePercentageAndRepaint(int percentage) {
		int tickOffset = 0;
		while (this.percentage != percentage) {
			if (tickOffset++ % 3500000 == 0) {
				this.percentage += 1;
				repaint();
			}
		}
	}

	/**
	 * Reset percentage and repaint.
	 */
	public void resetPercentageAndRepaint() {
		setPercentageAndRepaint(0);
	}

	/**
	 * Sets the percentage and repaint.
	 *
	 * @param percentage
	 *            the new percentage and repaint
	 */
	public void setPercentageAndRepaint(int percentage) {
		this.percentage = percentage;
		repaint();
	}

	/**
	 * Sets the message and repaint.
	 *
	 * @param message
	 *            the new message and repaint
	 */
	public void setMessageAndRepaint(String message) {
		this.message = message;
		repaint();
	}

	/**
	 * Gets the dialog.
	 *
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return this.dialog;
	}
}