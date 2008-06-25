package com.yoursway.ide.application.controllers.mainwindow;

import com.yoursway.databinding.IObservableValue;
import com.yoursway.databinding.WritableValue;

public class DocumentContentWindowModelImpl implements DocumentContentWindowModel {
    
    public final IObservableValue<String> data = WritableValue.withValueType(String.class);

    public IObservableValue<String> data() {
        return data;
    }
    
}
