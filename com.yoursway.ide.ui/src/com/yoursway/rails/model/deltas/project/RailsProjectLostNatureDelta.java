package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public class RailsProjectLostNatureDelta extends RailsProjectDelta {
    
    public RailsProjectLostNatureDelta(IRailsProject railsProject) {
        super(railsProject);
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitLostNatureProject(this);
    }
    
}
