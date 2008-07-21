package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.widgets.Composite;

public interface InsertionContent {
    
    void init(Composite parent);
    
    void updateSize();
    
    void redraw();
    
    void dispose();
    
    boolean isDisposed();
    
}
