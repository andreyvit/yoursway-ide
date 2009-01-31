package com.yoursway.web.html;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.eclipse.core.runtime.Assert;

public class HTMLReparser {
	private Source source;
	
	public HTMLReparser(String text) {
		source = new Source(text);
	}
	
	public static void main(String[] args) {
		HTMLReparser reparser = new HTMLReparser("<!DOCTYPE><html><div id='d1'></div><div id='d2'></div></html>");
		String html = reparser.getDOM();
		int realPosition = html.indexOf("d2") + 2;
//		reparser.source.get
//		int followedPosition = reparser.followPath(5, 2);
//		Assert.isTrue(realPosition == followedPosition);
	}

	public Element followPath(int index, int offset) {
		return source.getChildElements().get(index);
	}

	public void dispose() {
		
	}

	public String getDOM() {
		return source.getSourceFormatter().toString();
	}
}
