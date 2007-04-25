package com.yoursway.rails.model.deltas.project;

import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderDelta;

public class RailsProjectChangedDelta extends RailsProjectDelta {
    
    private final ControllersFolderDelta controllersFolderDelta;
    
    public RailsProjectChangedDelta(IRailsProject railsProject, ControllersFolderDelta controllersFolderDelta) {
        super(railsProject);
        this.controllersFolderDelta = controllersFolderDelta;
    }
    
    @Override
    public void accept(IRailsProjectDeltaVisitor visitor) {
        visitor.visitChangedProject(this);
    }
    
    public ControllersFolderDelta getControllersFolderDelta() {
        return controllersFolderDelta;
    }
    
}
