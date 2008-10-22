package com.yoursway.web.editing;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BrowserAdditions {
	protected boolean init = false;
	protected String text = "";
	private final Browser browser;

	/**
	 * Simple stupid differ
	 */
	public String getDiff(String update) {
		String oldtext = text;
		text = update;
		if (oldtext.equals(text))
			return null;
		if (text.length() > oldtext.length()) {
			// added
			int start = findMCS(oldtext, text, 1);
			int end = findMCS(oldtext, text, -1);
			if (end <= start + text.length() - oldtext.length() + 1)
				end = start + text.length() - oldtext.length() + 1;
			return "(added  ) [" + start + ":" + end + "] " + text.substring(start, end);
		} else if (text.length() <= oldtext.length()) {
			// removed or changed
			int start = findMCS(text, oldtext, 1);
			int end = findMCS(text, oldtext, -1);
			if (end <= start + oldtext.length() - text.length() + 1)
				end = start + oldtext.length() - text.length() + 1;
			if (start == end)
				return null;
			return "(removed) [" + start + ":" + end + "] "
					+ oldtext.substring(start, end);
		}
		return null;
	}

	private int findMCS(String src, String mask, int dir) {
		int start = 0;
		int mstart = 0;
		if (dir != 1) {
			start = src.length() - 1;
			mstart = mask.length() - 1;
		}
		while (start < src.length() && start >= 0 && mstart < mask.length()
				&& mstart >= 0) {
			if (dir == 1) {
				if (src.charAt(start) != mask.charAt(mstart))
					return start;
				start++;
				mstart++;
			} else {
				if (src.charAt(start) != mask.charAt(mstart))
					return start;
				start--;
				mstart--;
			}
		}
		return start;
	}

	public BrowserAdditions(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).align(
				SWT.FILL, SWT.FILL).create());
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				System.out.println("changed");
			}

			public void completed(ProgressEvent event) {
				if (!init) {
					browser
							.execute("document.body.contentEditable='true';document.designMode='on';");
					init = true;
					text = browser.getText();
					System.out.println(text);
				}
				System.out.println("completed");
			}
		});

		browser.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				catchUpWithPossibleEdits();
			}
		});

		browser.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				catchUpWithPossibleEdits();
			}
		});
		browser.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				catchUpWithPossibleEdits();
			}

			public void keyReleased(KeyEvent e) {
				catchUpWithPossibleEdits();
			}
		});

		GridLayoutFactory.fillDefaults().applyTo(parent);
	}

	private boolean isMac() {
		return ("cocoa".equals(SWT.getPlatform()) || "carbon".equals(SWT.getPlatform()));
	}

	public void catchUpWithPossibleEdits() {
		if (!init || browser.isDisposed())
			return;
		String diff = getDiff(browser.getText());
		if (diff != null)
			System.out.println(diff);
	}
	
	public Control getControl() {
		return browser;
	}

	public void dispose() {

	}

	public void setHtml(String data) {
		browser.setText(data);
	}
	
}
