package com.yoursway.ui.popup.anchors;

import org.eclipse.swt.graphics.Rectangle;

/**
 * Provides a point by applying an anchor to a rectangle provided by
 * <code>IRectangleProvider</code>.
 * 
 * @author Andrey Tarantsov
 */
public class InsetRectangleProvider implements IRectangleProvider {
    
    private final IRectangleProvider rectangleProvider;
    private final Insets insets;
    
    public InsetRectangleProvider(IRectangleProvider rectangleProvider, Insets insets) {
        this.rectangleProvider = rectangleProvider;
        this.insets = insets;
    }
    
    public Rectangle getRectangle() {
        Rectangle baseRect = rectangleProvider.getRectangle();
        return new Rectangle(baseRect.x + insets.getLeftInset(), baseRect.y + insets.getTopInset(),
                baseRect.width - insets.getLeftInset() - insets.getRightInset(), baseRect.height
                        - insets.getTopInset() - insets.getBottomInset());
    }
    
}
