package com.yoursway.ide.editors.text;

import java.io.File;

import org.eclipse.core.databinding.observable.value.IObservableValue;

import com.yoursway.ide.application.model.Document;

public interface DocumentContentWindowModel {
    
    IObservableValue<String> data();

	IObservableValue<File> file();

	Document document();
    
}
