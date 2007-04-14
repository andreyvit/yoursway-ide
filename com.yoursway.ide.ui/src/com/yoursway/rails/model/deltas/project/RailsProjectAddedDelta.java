package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public class RailsProjectAddedDelta extends RailsProjectDelta {
    
    public RailsProjectAddedDelta(IRailsProject railsProject) {
        super(railsProject);
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitAddedProject(this);
    }
    
}
