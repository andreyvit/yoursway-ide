/**
 * 
 */
package com.yoursway.rails.core.controllers.internal;

import com.yoursway.rails.core.controllers.IControllersListener;
import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.rails.core.internal.support.ComparingUpdater;

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