package com.yoursway.ide.editors.text;

import java.io.File;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

public class DocumentContentWindowModelImpl implements DocumentContentWindowModel {
    
    public final IObservableValue<String> data = WritableValue.withValueType(String.class);
    
    public final IObservableValue<File> file = WritableValue.withValueType(String.class);

    public IObservableValue<String> data() {
        return data;
    }

	public IObservableValue<File> file() {
		return file;
	}
    
}
