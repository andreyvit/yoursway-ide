package com.yoursway.web.editing;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.yoursway.utils.YsFileUtils;

public class BrowserAdditions {
	protected boolean init = false;
	protected String text = "";
	private final Browser browser;
	private BrowserCallback callback;

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
			if (end <= start + text.length() - oldtext.length())
				end = start + text.length() - oldtext.length();
			return "(added  ) [" + start + ":" + end + "] " + text.substring(start, end);
		} else if (text.length() <= oldtext.length()) {
			// removed or changed
			int start = findMCS(text, oldtext, 1);
			int end = findMCS(text, oldtext, -1);
			if (end <= start + oldtext.length() - text.length())
				end = start + oldtext.length() - text.length();
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
					try {
						String path = "/" + getClass().getName().replaceAll("\\.", "/").replaceFirst("/[^/]+$", "");
						InputStream stream = getClass().getClassLoader()
								.getResourceAsStream(path+"/editor.js");
						browser.execute(YsFileUtils.readAsStringAndClose(stream));
					} catch (IOException e) {
						throw new AssertionError(e);
					}
					init = true;
					text = browser.getText();
					System.out.println(text);
				}
				System.out.println("completed");
			}
		});

		GridLayoutFactory.fillDefaults().applyTo(parent);

		callback = new BrowserCallback(browser, "jDocumentChanged");
		callback.addDocumentChangedListener(new DocumentChangeListener(){

			@Override
			void changed(String newContents) {
				catchUpWithPossibleEdits(newContents);
			}
			
		});

	}

//	private boolean isMac() {
//		return ("cocoa".equals(SWT.getPlatform()) || "carbon".equals(SWT.getPlatform()));
//	}
//
	public void catchUpWithPossibleEdits(String text) {
		if (!init || browser.isDisposed())
			return;
		String diff = getDiff(text);
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

	public void setUrl(String url) {
		browser.setUrl(url);
	}

}
