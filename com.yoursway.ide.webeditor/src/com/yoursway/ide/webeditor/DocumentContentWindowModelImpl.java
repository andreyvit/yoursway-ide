package com.yoursway.ide.webeditor;

import java.io.File;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

public class DocumentContentWindowModelImpl implements DocumentContentWindowModel {
    
    private final IObservableValue<String> data = WritableValue.withValueType(String.class);
	private final IObservableValue<File> file = WritableValue.withValueType(File.class);

    public IObservableValue<String> data() {
        return data;
    }

	public IObservableValue<File> file() {
		return file;
	}
    
}
