package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.custom.ExtendedTextInternal;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public class Insertion {
    
    private final InsertionContent content;
    private int offset;
    private final Composite composite;
    
    private final ExtendedTextInternal extendedText;
    
    @UseFromUIThread
    public Insertion(InsertionContent content, int offset, Composite composite,
            ExtendedTextInternal extendedText) {
        if (content == null)
            throw new NullPointerException("content is null");
        if (composite == null)
            throw new NullPointerException("composite is null");
        if (extendedText == null)
            throw new NullPointerException("extendedText is null");
        
        this.content = content;
        this.offset = offset;
        this.composite = composite;
        
        this.extendedText = extendedText;
        
        updateLocation();
    }
    
    @UseFromAnyThread
    public int offset() {
        return offset;
    }
    
    public void offset(int offset) {
        this.offset = offset;
    }
    
    @UseFromAnyThread
    public InsertionContent content() {
        return content;
    }
    
    @UseFromUIThread
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
    
    @UseFromUIThread
    public void dispose() {
        if (!content.isDisposed())
            content.dispose();
        if (!composite.isDisposed())
            composite.dispose();
    }
    
    @UseFromUIThread
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
    
    @UseFromUIThread
    public Point getLocation() {
        return composite.getLocation();
    }
    
}
