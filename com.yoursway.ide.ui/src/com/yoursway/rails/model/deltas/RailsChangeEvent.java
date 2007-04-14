package com.yoursway.rails.model.deltas;

import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.model.IRails;
import com.yoursway.rails.model.deltas.project.IRailsProjectDeltaVisitor;
import com.yoursway.rails.model.deltas.project.RailsProjectDelta;
import com.yoursway.rails.model.internal.Rails;

public class RailsChangeEvent {
    
    private final Rails rails;
    
    private final RailsProjectDelta[] projectDeltas;
    
    public RailsChangeEvent(Rails rails, RailsProjectDelta[] projectDeltas) {
        Assert.isLegal(rails != null);
        this.rails = rails;
        this.projectDeltas = (projectDeltas == null ? RailsProjectDelta.EMPTY_ARRAY : projectDeltas);
    }
    
    public IRails getRails() {
        return rails;
    }
    
    public RailsProjectDelta[] getProjectDeltas() {
        return projectDeltas;
    }
    
    public void accept(IRailsProjectDeltaVisitor visitor) {
        for (RailsProjectDelta delta : projectDeltas) {
            delta.accept(visitor);
        }
    }
    
}
