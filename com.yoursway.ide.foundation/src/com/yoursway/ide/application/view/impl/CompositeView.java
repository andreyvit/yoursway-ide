package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.application.view.View;
import com.yoursway.ide.application.view.ViewCallback;

public class CompositeView implements View {
    
    public CompositeView(Composite composite, ViewCallback callback) {
        if (composite == null)
            throw new NullPointerException("composite is null");
        if (callback == null)
            throw new NullPointerException("callback is null");
        callback.bindPresentation(new CompositeViewPresentation(composite));
    }

}
