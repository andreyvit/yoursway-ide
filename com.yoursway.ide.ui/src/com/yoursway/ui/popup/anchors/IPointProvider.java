package com.yoursway.ui.popup.anchors;

import org.eclipse.swt.graphics.Point;

/**
 * Provides a point upon request. The coordinates are relative to the display.
 * 
 * @author Andrey Tarantsov
 */
public interface IPointProvider {
    
    Point getPoint();
    
}
