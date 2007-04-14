package com.yoursway.rails.model.internal;

import java.util.Collection;

import com.yoursway.rails.model.deltas.RailsChangeEvent;
import com.yoursway.rails.model.deltas.project.RailsProjectDelta;

public class RailsDeltaBuilder {
    
    private boolean somethingChanged = false;
    private final Rails rails;
    
    private Collection<RailsProjectDelta> projectDeltasCollection;
    
    public RailsDeltaBuilder(Rails rails) {
        this.rails = rails;
    }
    
    public void add(RailsProjectDelta delta) {
        projectDeltasCollection.add(delta);
    }
    
    void somethingChanged() {
        somethingChanged = true;
    }
    
    public RailsChangeEvent build() {
        RailsProjectDelta[] projectDeltas = projectDeltasCollection.toArray(RailsProjectDelta.EMPTY_ARRAY);
        return new RailsChangeEvent(rails, projectDeltas);
    }
    
}
