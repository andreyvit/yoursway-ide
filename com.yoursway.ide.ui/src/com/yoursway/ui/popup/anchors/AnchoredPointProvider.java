package com.yoursway.ui.popup.anchors;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Provides a point by applying an anchor to a rectangle provided by
 * <code>IRectangleProvider</code>.
 * 
 * @author Andrey Tarantsov
 */
public class AnchoredPointProvider implements IPointProvider {
    
    private final IRectangleProvider rectangleProvider;
    private final Anchor anchor;
    
    public AnchoredPointProvider(IRectangleProvider rectangleProvider, Anchor anchor) {
        this.rectangleProvider = rectangleProvider;
        this.anchor = anchor;
    }
    
    public Point getPoint() {
        Rectangle rectangle = rectangleProvider.getRectangle();
        return anchor.anchor(rectangle);
    }
    
}
