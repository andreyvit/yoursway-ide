package com.yoursway.ide.application.controllers.mainwindow;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

import com.yoursway.ide.application.view.mainwindow.EditorWindowModel;

public class EditorWindowModelImpl implements EditorWindowModel {
    
    public final IObservableValue<String> title = WritableValue.withValueType(String.class);

    public IObservableValue<String> title() {
        return title;
    }
    
}
