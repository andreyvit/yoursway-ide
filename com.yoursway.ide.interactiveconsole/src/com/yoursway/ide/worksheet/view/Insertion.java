package com.yoursway.ide.worksheet.view;

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
        Point location = extendedText.getLocationAtOffset(offset);
        if (!composite.getLocation().equals(location)) {
            composite.setLocation(location);
            content.redraw(); //? ineffective //> don't do it every time
        }
    }
    
    public void updateSize() {
        content.updateSize();
    }
    
    public void dispose() {
        if (!content.isDisposed())
            content.dispose();
        if (!composite.isDisposed())
            composite.dispose();
        
    }
    
    public Rectangle getBounds() {
        return composite.getBounds();
    }
    
    public void setLocation(int x, int y) {
        composite.setLocation(x, y);
    }
    
}
