package com.yoursway.web.editing.django.wrapper;

import static java.lang.String.format;

public class Fragment {

	public int start;
	public final int id;
	public final int originStart;
	public final int originEnd;
	public final Source origin;
	public final int end;

	public Fragment(int id, int originStart, int originEnd, Source origin, int start, int end) {
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
