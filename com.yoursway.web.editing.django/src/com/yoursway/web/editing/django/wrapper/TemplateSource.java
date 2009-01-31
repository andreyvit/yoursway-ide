package com.yoursway.web.editing.django.wrapper;

public class TemplateSource {
	public final int id;
	public final String type;
	public final String fileName;

	public TemplateSource(int id, String type, String fileName) {
		this.id = id;
		this.type = type;
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return String.format("Source(%s,%s)", type, fileName);
	}
	
}
