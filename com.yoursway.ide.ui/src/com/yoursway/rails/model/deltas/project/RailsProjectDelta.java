package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public abstract class RailsProjectDelta {
    
    public static final RailsProjectDelta[] EMPTY_ARRAY = new RailsProjectDelta[0];
    
    private final IRailsProject railsProject;
    
    public RailsProjectDelta(IRailsProject railsProject) {
        this.railsProject = railsProject;
    }
    
    public IRailsProject getRailsProject() {
        return railsProject;
    }
    
    public abstract void accept(IRailsProjectDeltaVisitor visitor);
    
}
