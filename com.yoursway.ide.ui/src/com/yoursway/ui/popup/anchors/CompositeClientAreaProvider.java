package com.yoursway.ui.popup.anchors;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * Provides the client area of the given composite. The coordinates are relative
 * to the display.
 * 
 * @author Andrey Tarantsov
 */
public class CompositeClientAreaProvider implements IRectangleProvider {
    
    private final Composite control;
    
    public CompositeClientAreaProvider(Composite control) {
        this.control = control;
    }
    
    public Rectangle getRectangle() {
        return Geometry.toDisplay(control, control.getClientArea());
    }
    
}
