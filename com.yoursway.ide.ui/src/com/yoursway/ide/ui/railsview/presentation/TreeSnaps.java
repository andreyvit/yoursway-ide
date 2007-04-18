/**
 * 
 */
package com.yoursway.ide.ui.railsview.presentation;

import java.util.Collection;

import org.eclipse.swt.widgets.Tree;

import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.anchors.Anchor;
import com.yoursway.ui.popup.anchors.AnchoredPointProvider;
import com.yoursway.ui.popup.anchors.CompositeClientAreaProvider;
import com.yoursway.ui.popup.anchors.IRectangleProvider;
import com.yoursway.ui.popup.menu.SnapToMenuItems;

public class TreeSnaps {
    
    private final SnapPosition bottomRight;
    private final SnapPosition topRight;
    
    public TreeSnaps(Tree tree) {
        IRectangleProvider boundsProvider = new CompositeClientAreaProvider(tree);
        bottomRight = new SnapPosition(new AnchoredPointProvider(boundsProvider, Anchor.BOTTOM_RIGHT),
                Anchor.BOTTOM_RIGHT, "treeBottomRight");
        topRight = new SnapPosition(new AnchoredPointProvider(boundsProvider, Anchor.TOP_RIGHT),
                Anchor.TOP_RIGHT, "treeTopRight");
    }
    
    public void addTo(Collection<SnapPosition> snaps) {
        snaps.add(bottomRight);
        snaps.add(topRight);
    }
    
    public void addTo(SnapToMenuItems menuItems) {
        menuItems.add(bottomRight, "Snap to lower right corner");
        menuItems.add(topRight, "Snap to upper right corner");
    }
    
}