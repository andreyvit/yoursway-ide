package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public class RailsProjectRemovedDelta extends RailsProjectDelta {
    
    public RailsProjectRemovedDelta(IRailsProject railsProject) {
        super(railsProject);
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitRemovedProject(this);
    }
    
}
