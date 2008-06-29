package com.yoursway.ide.editors.text;

import org.eclipse.core.databinding.observable.value.IObservableValue;

public interface DocumentContentWindowModel {
    
    IObservableValue<String> data();
    
}
