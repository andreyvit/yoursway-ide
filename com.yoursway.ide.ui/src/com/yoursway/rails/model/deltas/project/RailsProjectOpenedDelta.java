package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public class RailsProjectOpenedDelta extends RailsProjectDelta {
    
    public RailsProjectOpenedDelta(IRailsProject railsProject) {
        super(railsProject);
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitOpenedProject(this);
    }
    
}
