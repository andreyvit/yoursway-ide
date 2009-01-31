package com.yoursway.web.editing;

import java.util.ArrayList;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public class BrowserCallback extends BrowserFunction {

	private ArrayList<DocumentChangeListener> listeners = new ArrayList<DocumentChangeListener>();

	public BrowserCallback(Browser browser, String name) {
		super(browser, name);
	}

	void addDocumentChangedListener(DocumentChangeListener listener){
		listeners.add(listener);
	}
	
	void removeDocumentChangedListener(DocumentChangeListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public Object function(Object[] arguments) {
		for (DocumentChangeListener listener : listeners) {
			listener.changed((String)arguments[0]);
		}
		return null;
	}
}
