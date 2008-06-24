package com.mkalugin.corchy.internal.ui.location;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;

public enum ShellPositionConstraint {
    
    INTERSECTS_ANY_MONITOR {
        
        @Override
        public Rectangle constrain(Rectangle preferredDimensions, Display display) {
            if (intersectsAnyMonitor(preferredDimensions, display))
                return preferredDimensions;
            else
                return constrainToSingleMonitor(preferredDimensions, display);
        }
        
    },
    
    CONTAINED_WITHIN_SINGLE_MONITOR {
        
        @Override
        public Rectangle constrain(Rectangle preferredDimensions, Display display) {
            return constrainToSingleMonitor(preferredDimensions, display);
        }
        
    };
    
    public abstract Rectangle constrain(Rectangle preferredDimensions, Display display);
    
    private static Rectangle constrainToSingleMonitor(Rectangle preferredDimensions, Display display) {
        Rectangle result = new Rectangle(preferredDimensions.x, preferredDimensions.y,
                preferredDimensions.width, preferredDimensions.height);
        
        Monitor mon = getClosestMonitor(display, Geometry.centerPoint(result));
        
        Rectangle bounds = mon.getClientArea();
        
        if (result.height > bounds.height) {
            result.height = bounds.height;
        }
        
        if (result.width > bounds.width) {
            result.width = bounds.width;
        }
        
        result.x = Math.max(bounds.x, Math.min(result.x, bounds.x + bounds.width - result.width));
        result.y = Math.max(bounds.y, Math.min(result.y, bounds.y + bounds.height - result.height));
        
        return result;
    }
    
    /**
     * Returns the monitor whose client area contains the given point. If no
     * monitor contains the point, returns the monitor that is closest to the
     * point. If this is ever made public, it should be moved into a separate
     * utility class.
     * 
     * @param toSearch
     *            point to find (display coordinates)
     * @param toFind
     *            point to find (display coordinates)
     * @return the montor closest to the given point
     */
    private static Monitor getClosestMonitor(Display toSearch, Point toFind) {
        int closest = Integer.MAX_VALUE;
        
        Monitor[] monitors = toSearch.getMonitors();
        Monitor result = monitors[0];
        
        for (int idx = 0; idx < monitors.length; idx++) {
            Monitor current = monitors[idx];
            
            Rectangle clientArea = current.getClientArea();
            
            if (clientArea.contains(toFind)) {
                return current;
            }
            
            int distance = Geometry.distanceSquared(Geometry.centerPoint(clientArea), toFind);
            if (distance < closest) {
                closest = distance;
                result = current;
            }
        }
        
        return result;
    }
    
    /**
     * 
     * Returns true iff the given rectangle is located in the client area of any
     * monitor.
     * 
     * @param someRectangle
     *            a rectangle in display coordinates (not null)
     * @return true iff the given point can be seen on any monitor
     */
    public static boolean intersectsAnyMonitor(Rectangle someRectangle, Display display) {
        Monitor[] monitors = display.getMonitors();
        
        for (int idx = 0; idx < monitors.length; idx++) {
            Monitor mon = monitors[idx];
            
            if (mon.getClientArea().intersects(someRectangle)) {
                return true;
            }
        }
        
        return false;
    }
    
}