package com.yoursway.ide.editors.text;

import java.io.File;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

import com.yoursway.ide.application.model.Document;

public class DocumentContentWindowModelImpl implements DocumentContentWindowModel {
    
    public final IObservableValue<String> data = WritableValue.withValueType(String.class);
    
    public final IObservableValue<File> file = WritableValue.withValueType(File.class);

	private final Document document;

    public DocumentContentWindowModelImpl(Document document) {
		this.document = document;
	}
    
    public Document document() {
		return document;
	}

	public IObservableValue<String> data() {
        return data;
    }

	public IObservableValue<File> file() {
		return file;
	}
    
}
