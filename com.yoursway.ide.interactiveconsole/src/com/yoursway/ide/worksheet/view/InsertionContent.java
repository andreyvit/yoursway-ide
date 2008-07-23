package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public interface InsertionContent {
    
    @UseFromUIThread
    void init(Composite composite, ListenersAcceptor la);
    
    @UseFromUIThread
    void redraw();
    
    @UseFromUIThread
    void dispose();
    
    @UseFromAnyThread
    boolean isDisposed();
    
}
