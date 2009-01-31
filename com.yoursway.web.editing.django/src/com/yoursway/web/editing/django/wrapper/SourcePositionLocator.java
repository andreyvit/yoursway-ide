package com.yoursway.web.editing.django.wrapper;

public interface SourcePositionLocator {

	public abstract TemplateSegment find(int position);

}