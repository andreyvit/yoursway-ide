package com.yoursway.ui.popup.anchors;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * Provides the client area of the given composite. The coordinates are relative
 * to the display.
 * 
 * @author Andrey Tarantsov
 */
public class ControlBoundsProvider implements IRectangleProvider {
    
    private final Control control;
    
    public ControlBoundsProvider(Control control) {
        this.control = control;
    }
    
    public Rectangle getRectangle() {
        return Geometry.toDisplay(control.getParent(), control.getBounds());
    }
    
}
