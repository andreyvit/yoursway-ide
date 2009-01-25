package com.yoursway.web.editing.django.wrapper;

import static java.lang.String.format;

public class TemplateSegment {

	public int start;
	public final int id;
	public final int originStart;
	public final int originEnd;
	public final TemplateSource origin;
	public final int end;

	public TemplateSegment(int id, int originStart, int originEnd, TemplateSource origin, int start, int end) {
		this.id = id;
		this.originStart = originStart;
		this.originEnd = originEnd;
		this.origin = origin;
		this.start = start;
		this.end = end;
		
	}
	
	@Override
	public String toString() {
		return format("origin=<%d,%d> html=<%d,%d> in '%s'", originStart, originEnd, start, end, origin);
	}
	
}
