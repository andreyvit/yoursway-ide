/**
 * 
 */
package com.yoursway.rails.core.models.internal;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.models.IModelsListener;
import com.yoursway.rails.core.models.RailsModel;

public final class BroadcastingRailsModelsChangeVisitor implements ComparingUpdater.IVisitor<RailsModel> {
    private final Iterable<IModelsListener> listeners;
    
    public BroadcastingRailsModelsChangeVisitor(Iterable<IModelsListener> iterable) {
        this.listeners = iterable;
    }
    
    public void visitAdded(RailsModel value) {
        for (IModelsListener listener : listeners)
            listener.modelAdded(value);
    }
    
    public void visitRemoved(RailsModel value) {
        for (IModelsListener listener : listeners)
            listener.modelRemoved(value);
    }
}