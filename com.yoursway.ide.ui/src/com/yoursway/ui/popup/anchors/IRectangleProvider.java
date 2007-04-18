package com.yoursway.ui.popup.anchors;

import org.eclipse.swt.graphics.Rectangle;

/**
 * Provides a rectangle upon request. The coordinates are relative to the
 * display.
 * 
 * @author Andrey Tarantsov
 */
public interface IRectangleProvider {
    
    Rectangle getRectangle();
    
}
