package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;

public class RailsProjectObtainedNatureDelta extends RailsProjectDelta {
    
    public RailsProjectObtainedNatureDelta(IRailsProject railsProject) {
        super(railsProject);
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitObtainedNatureProject(this);
    }
    
}
