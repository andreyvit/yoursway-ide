package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;

public interface Insertion {
    
    void createWidget(Composite parent);
    
    @Deprecated
    void offset(int offset);
    
    @Deprecated
    int offset();
    
    @Deprecated
    void updateOffset(VerifyEvent e);
    
    @Deprecated
    boolean disposed();
    
    void updateLocation(PaintObjectEvent e);
    
    void updateSize();
    
}
