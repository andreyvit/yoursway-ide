package com.yoursway.rails.model.internal;

import com.yoursway.rails.model.IRailsChangeListener;

public class RailsDeltaBuilder {
    
    private boolean somethingChanged = false;
    private final Rails rails;
    
    public RailsDeltaBuilder(Rails rails) {
        this.rails = rails;
    }
    
    void somethingChanged() {
        somethingChanged = true;
    }
    
    public void fire(IRailsChangeListener[] railsChangeListeners) {
        if (somethingChanged) {
            RailsChangeEvent event = new RailsChangeEvent(rails);
            for (IRailsChangeListener listener : railsChangeListeners) {
                listener.railsModelChanged(event);
            }
        }
    }
    
}
