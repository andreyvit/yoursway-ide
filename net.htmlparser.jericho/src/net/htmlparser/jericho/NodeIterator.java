// Jericho HTML Parser - Java based library for analysing and manipulating HTML
// Version 3.0-beta1
// Copyright (C) 2007 Martin Jericho
// http://jerichohtml.sourceforge.net/
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of either one of the following licences:
//
// 1. The Eclipse Public License (EPL) version 1.0,
// included in this distribution in the file licence-epl-1.0.html
// or available at http://www.eclipse.org/legal/epl-v10.html
//
// 2. The GNU Lesser General Public License (LGPL) version 2.1 or later,
// included in this distribution in the file licence-lgpl-2.1.txt
// or available at http://www.gnu.org/licenses/lgpl.txt
//
// This library is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the individual licence texts for more details.

package net.htmlparser.jericho;

import java.util.*;

/**
 * Iterates over the "nodes" in a segment.
 * <p>
 * Every object returned is a Segment.  All tags found with the Segment.getAllTags() method are included, as well as segments representing the text in between them.
 */
class NodeIterator implements Iterator<Segment> {
	private final Segment segment;
	private int pos;
	private Tag nextTag;

	public NodeIterator(final Segment segment) {
		this.segment=segment;
		if (segment==segment.source) segment.source.fullSequentialParse();
		pos=segment.getBegin();
		nextTag=segment.source.getNextTag(pos);
		if (nextTag!=null && nextTag.getBegin()>=segment.getEnd()) nextTag=null;
	}

	public boolean hasNext() {
		return pos<segment.getEnd() || nextTag!=null;
	}	

	public Segment next() {
		final int oldPos=pos;
		if (nextTag!=null) {
			if (oldPos<nextTag.getBegin()) return new Segment(segment.getSource(),oldPos,pos=nextTag.getBegin());
			final Tag tag=nextTag;
			nextTag=nextTag.getNextTag();
			if (nextTag!=null && nextTag.getBegin()>=segment.getEnd()) nextTag=null;
			if (pos<tag.getEnd()) pos=tag.getEnd();
			return tag;
		} else {
			if (!hasNext()) throw new NoSuchElementException();
			return new Segment(segment.getSource(),oldPos,pos=segment.getEnd());
		}
	}

	public void skipToPos(final int pos) {
		if (pos<this.pos) return; // can't go backwards
		this.pos=pos;
		nextTag=segment.source.getNextTag(pos);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}

