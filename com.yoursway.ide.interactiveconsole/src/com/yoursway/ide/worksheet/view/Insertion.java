package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.widgets.Composite;

public interface Insertion {
    
    void createWidget(Composite parent, ResizingListener listener);
    
    void updateLocation(PaintObjectEvent e);
    
    void updateSize();
    
    void dispose();
    
}
