package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public class RailsProjectClosedDelta extends RailsProjectDelta {
    
    public RailsProjectClosedDelta(IRailsProject railsProject) {
        super(railsProject);
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitClosedProject(this);
    }
    
}
