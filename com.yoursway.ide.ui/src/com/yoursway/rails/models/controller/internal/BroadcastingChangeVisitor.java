/**
 * 
 */
package com.yoursway.rails.models.controller.internal;

import com.yoursway.rails.models.ComparingUpdater;
import com.yoursway.rails.models.controller.IControllersListener;
import com.yoursway.rails.models.controller.RailsController;

public final class BroadcastingChangeVisitor implements ComparingUpdater.IVisitor<RailsController> {
    private final IControllersListener[] listeners;
    
    public BroadcastingChangeVisitor(IControllersListener[] listeners) {
        this.listeners = listeners;
    }
    
    public void visitAdded(RailsController value) {
        for (IControllersListener listener : listeners)
            listener.controllerAdded(value);
    }
    
    public void visitRemoved(RailsController value) {
        for (IControllersListener listener : listeners)
            listener.controllerRemoved(value);
    }
}