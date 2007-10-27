/**
 * 
 */
package com.yoursway.ide.ui.railsview.presentation;

import java.awt.Insets;
import java.util.Collection;

import org.eclipse.swt.widgets.Control;

public class ControlSnaps {
    
    private final SnapPosition bottomLeft;
    private final SnapPosition bottomRight;
    private final SnapPosition topLeft;
    private final SnapPosition topRight;
    
    public ControlSnaps(Control control) {
        Insets verticalInsets = new Insets(1, 0);
        IRectangleProvider boundsProvider = new InsetRectangleProvider(new ControlBoundsProvider(control),
                verticalInsets);
        bottomLeft = new SnapPosition(new AnchoredPointProvider(boundsProvider, Anchor.BOTTOM_LEFT),
                Anchor.TOP_LEFT, "itemBottomLeft");
        bottomRight = new SnapPosition(new AnchoredPointProvider(boundsProvider, Anchor.BOTTOM_RIGHT),
                Anchor.TOP_LEFT, "itemBottomRight");
        topLeft = new SnapPosition(new AnchoredPointProvider(boundsProvider, Anchor.TOP_LEFT),
                Anchor.BOTTOM_LEFT, "itemTopLeft");
        topRight = new SnapPosition(new AnchoredPointProvider(boundsProvider, Anchor.TOP_RIGHT),
                Anchor.BOTTOM_LEFT, "itemTopRight");
    }
    
    public void addTo(Collection<SnapPosition> snaps) {
        snaps.add(bottomLeft);
        snaps.add(bottomRight);
        snaps.add(topLeft);
        snaps.add(topRight);
    }
    
    public void addTo(SnapToMenuItems menuItems) {
        menuItems.add(bottomLeft, "Snap to bottom left");
        menuItems.add(topLeft, "Snap to top left");
        menuItems.add(bottomRight, "Snap to bottom right");
        menuItems.add(topRight, "Snap to top right");
    }
    
}