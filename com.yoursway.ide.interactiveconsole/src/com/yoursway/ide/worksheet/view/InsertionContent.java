package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.widgets.Composite;

public interface InsertionContent {
    
    void init(Composite composite, ListenersAcceptor la);
    
    void redraw();
    
    void dispose();
    
    boolean isDisposed();
    
}
