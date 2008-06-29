package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.application.view.ViewPresentation;

public class CompositeViewPresentation implements ViewPresentation {

    private final Composite composite;

    public CompositeViewPresentation(Composite composite) {
        if (composite == null)
            throw new NullPointerException("composite is null");
        this.composite = composite;
    }

    public Composite composite() {
        return composite;
    }
    
}
