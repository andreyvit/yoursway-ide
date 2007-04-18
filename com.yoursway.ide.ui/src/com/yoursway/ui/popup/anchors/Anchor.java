package com.yoursway.ui.popup.anchors;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Determines a point on the given rectangle.
 * 
 * @author Andrey Tarantsov
 */
public interface Anchor {
    
    Point anchor(Rectangle snappee);
    
    public static final Anchor TOP_LEFT = new Anchor() {
        
        public Point anchor(Rectangle snappee) {
            return new Point(snappee.x, snappee.y);
        }
        
    };
    
    public static final Anchor TOP_RIGHT = new Anchor() {
        
        public Point anchor(Rectangle snappee) {
            return new Point(snappee.x + snappee.width, snappee.y);
        }
        
    };
    
    public static final Anchor BOTTOM_LEFT = new Anchor() {
        
        public Point anchor(Rectangle snappee) {
            return new Point(snappee.x, snappee.y + snappee.height);
        }
        
    };
    
    public static final Anchor BOTTOM_RIGHT = new Anchor() {
        
        public Point anchor(Rectangle snappee) {
            return new Point(snappee.x + snappee.width, snappee.y + snappee.height);
        }
        
    };
    
}
