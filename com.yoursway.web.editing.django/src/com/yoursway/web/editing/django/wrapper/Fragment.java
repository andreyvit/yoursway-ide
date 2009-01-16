package com.yoursway.web.editing.django.wrapper;

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
	
}
