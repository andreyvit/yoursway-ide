package com.yoursway.ui.popup.anchors;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Provides a rectangle occupied by the given tree item. The coordinates are
 * relative to the display.
 * 
 * @author Andrey Tarantsov
 */
public class TreeItemBoundsProvider implements IRectangleProvider {
    
    private final TreeItem treeItem;
    
    public TreeItemBoundsProvider(TreeItem treeItem) {
        this.treeItem = treeItem;
    }
    
    public Rectangle getRectangle() {
        return Geometry.toDisplay(treeItem.getParent(), treeItem.getBounds());
    }
    
}
