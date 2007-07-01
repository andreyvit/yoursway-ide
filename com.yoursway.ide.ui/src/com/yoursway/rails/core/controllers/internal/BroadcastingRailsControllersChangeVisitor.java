/**
 * 
 */
package com.yoursway.rails.core.controllers.internal;

import com.yoursway.rails.core.controllers.IRailsControllersListener;
import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.rails.core.internal.support.ComparingUpdater;

public final class BroadcastingRailsControllersChangeVisitor implements ComparingUpdater.IVisitor<RailsController> {
    private final IRailsControllersListener[] listeners;
    
    public BroadcastingRailsControllersChangeVisitor(IRailsControllersListener[] listeners) {
        this.listeners = listeners;
    }
    
    public void visitAdded(RailsController value) {
        for (IRailsControllersListener listener : listeners)
            listener.controllerAdded(value);
    }
    
    public void visitRemoved(RailsController value) {
        for (IRailsControllersListener listener : listeners)
            listener.controllerRemoved(value);
    }
}