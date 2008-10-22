package com.yoursway.ide.webeditor;

import java.io.File;

import org.eclipse.core.databinding.observable.value.IObservableValue;

public interface DocumentContentWindowModel {
    
    IObservableValue<String> data();

	IObservableValue<File> file();
    
}
