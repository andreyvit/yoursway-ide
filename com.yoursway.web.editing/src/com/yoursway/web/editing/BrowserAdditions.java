package com.yoursway.web.editing;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.utils.YsFileUtils;
import com.yoursway.web.editing.django.wrapper.SourcePositionLocator;
import com.yoursway.web.editing.django.wrapper.TemplateSegment;
import com.yoursway.web.html.HTMLReparser;

public class BrowserAdditions {
	protected boolean init = false;
	protected String text = "";
	private final Browser browser;
	private final SourcePositionLocator locator;
	private HTMLReparser reparser;

	public BrowserAdditions(Composite parent, SourcePositionLocator locator) {
		this.locator = locator;
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

		new BrowserFunction(browser, "jDocumentChanged") {
			@Override
			public Object function(Object[] arguments) {
				catchUpWithPossibleEdits((String) arguments[0]);
				return null;
			}
		};
		
		new BrowserFunction(browser, "jGoToSource") {
			@Override
			public Object function(Object[] arguments) {
				goToSource((String) arguments[0]);
				return null;
			}
		};

	}

//	private boolean isMac() {
//		return ("cocoa".equals(SWT.getPlatform()) || "carbon".equals(SWT.getPlatform()));
//	}
//
	public void catchUpWithPossibleEdits(String newText) {
		if (!init || browser.isDisposed())
			return;
		String diff = TextDiff.getDiff(text, newText);
		if (diff != null){
			text = newText;
			System.out.println(diff);
		}
	}

	public Control getControl() {
		return browser;
	}

	public void dispose() {
		reparser.dispose();
		browser.dispose();
	}

	public void setHtml(String data) {
		reparser = new HTMLReparser(data);
		String dom = reparser.getDOM();
		text = dom;
		browser.setText(dom);
	}

	public void setUrl(String url) {
		browser.setUrl(url);
	}
	
	public void goToSource(String arguments) {
		String[] path = arguments.split("/");
		
//		Node node = reparser.followPath(path);
		
//		TemplateSegment sourceFragment = locator.find(offset);
//		System.out.println(sourceFragment);
		
		createWindow(arguments, 0);
	}

	private void createWindow(String text, int offset) {
		final Display display = browser.getDisplay();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final StyledText styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);

		
		
		styledText.setText(text);
		styledText.setSelection(offset);
		
		shell.setSize(700, 400);
		shell.open();
	}
}
