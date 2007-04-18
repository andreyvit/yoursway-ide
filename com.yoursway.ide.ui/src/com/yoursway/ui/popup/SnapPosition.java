package com.yoursway.ui.popup;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ui.popup.anchors.Anchor;
import com.yoursway.ui.popup.anchors.IPointProvider;

/**
 * Represents a position to which rectangular objects may snap.
 * 
 * Clients may extend this class to store additional information identifying the
 * snap position.
 * 
 * @author Andrey Tarantsov
 */
public class SnapPosition implements ISnapPosition {
    
    private final Anchor anchor;
    private final IPointProvider pointProvider;
    private final String id;
    
    public SnapPosition(IPointProvider pointProvider, Anchor anchor, String id) {
        this.pointProvider = pointProvider;
        this.anchor = anchor;
        this.id = id;
    }
    
    protected Point getPoint() {
        return pointProvider.getPoint();
    }
    
    private Point alignTopLeftCornerOf(Rectangle snappee) {
        Point anchorPosition = anchor.anchor(snappee);
        Point point = getPoint();
        return new Point(point.x - (anchorPosition.x - snappee.x), point.y - (anchorPosition.y - snappee.y));
    }
    
    /* (non-Javadoc)
     * @see com.yoursway.ui.popup.ISnapPosition#calculate(org.eclipse.swt.graphics.Point, org.eclipse.swt.widgets.Composite)
     */
    public Point calculate(Point snappeeSize, Composite parentControl) {
        Point pos = alignTopLeftCornerOf(new Rectangle(0, 0, snappeeSize.x, snappeeSize.y));
        Rectangle displayBounds = parentControl.getDisplay().getClientArea();
        Point dPos = pos;
        Rectangle dPopupRect = Geometry.createRectangle(dPos, snappeeSize);
        Geometry.moveInside(dPopupRect, displayBounds);
        return new Point(dPopupRect.x, dPopupRect.y);
    }
    
    public String getId() {
        return id;
    }
    
}
