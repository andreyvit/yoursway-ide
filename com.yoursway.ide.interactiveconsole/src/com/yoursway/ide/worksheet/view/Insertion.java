package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public interface Insertion {
    
    //    void createWidget(Composite parent, ResizingListener listener);
    
    void updateLocation();
    
    void updateSize();
    
    void dispose();
    
    void createWidget(Composite parent, ResizingListener listener, ExtendedTextForInsertion ext);
    
    Rectangle getBounds();
    
    void setLocation(int x, int y);
    
}
