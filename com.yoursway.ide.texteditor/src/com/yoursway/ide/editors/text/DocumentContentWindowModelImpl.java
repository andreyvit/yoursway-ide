package com.yoursway.ide.editors.text;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

public class DocumentContentWindowModelImpl implements DocumentContentWindowModel {
    
    public final IObservableValue<String> data = WritableValue.withValueType(String.class);

    public IObservableValue<String> data() {
        return data;
    }
    
}
