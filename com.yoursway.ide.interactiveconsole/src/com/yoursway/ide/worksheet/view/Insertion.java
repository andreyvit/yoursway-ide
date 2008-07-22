package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.custom.ExtendedTextInternal;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class Insertion {
    
    private final InsertionContent content;
    private int offset;
    private final Composite composite;
    
    private final ExtendedTextInternal extendedText;
    
    public Insertion(InsertionContent insertion, int offset, Composite composite,
            ExtendedTextInternal extendedText) {
        this.content = insertion;
        this.offset = offset;
        this.composite = composite;
        
        this.extendedText = extendedText;
        
        updateLocation();
    }
    
    public int offset() {
        return offset;
    }
    
    public void offset(int offset) {
        this.offset = offset;
    }
    
    public InsertionContent content() {
        return content;
    }
    
    public void updateLocation() {
        if (!composite.getVisible()) {
            composite.setVisible(true);
            composite.redraw();
        }
        
        Point location = extendedText.getLocationAtOffset(offset);
        if (!composite.getLocation().equals(location)) {
            setLocation(location.x, location.y);
            
            composite.redraw();
            content.redraw(); //? ineffective //> don't do it every time
        }
    }
    
    public void dispose() {
        if (!content.isDisposed())
            content.dispose();
        if (!composite.isDisposed())
            composite.dispose();
    }
    
    public void setLocation(int x, int y) {
        composite.setLocation(x, y);
        
        Rectangle bounds = composite.getBounds();
        if (bounds.width == 0)
            bounds.width = 1; //! hack for intersects(...)
        if (bounds.height == 0)
            bounds.height = 1;
        
        if (!bounds.intersects(extendedText.getClientArea()))
            composite.setVisible(false);
    }
    
    public Point getLocation() {
        return composite.getLocation();
    }
    
}
