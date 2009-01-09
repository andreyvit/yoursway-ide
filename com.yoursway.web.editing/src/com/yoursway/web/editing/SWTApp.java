package com.yoursway.web.editing;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTApp implements IApplication {
	protected BrowserAdditions ba;

	public Object start(IApplicationContext context) throws Exception {
		Display.setAppName("WYSIWYG Editor");
		Display display = new Display();
		
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Text");

		ba = new BrowserAdditions(shell);
		ba.setHtml("My te<b>shj</b>gshdjklcsaniou");
//		ba.setUrl("http://google.com/");

		shell.setSize(700, 600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		
		return IApplication.EXIT_OK;
	}

	public void stop() {
		ba.dispose();
	}
}
