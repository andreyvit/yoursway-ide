package com.yoursway.web.editing.django.wrapper;

public class Source {
	public final int id;
	public final String type;
	public final String fileName;

	public Source(int id, String type, String fileName) {
		this.id = id;
		this.type = type;
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return String.format("Source(%s,%s)", type, fileName);
	}
	
}
