package com.yoursway.rails.model.internal.deltas;

import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderDelta;
import com.yoursway.rails.model.deltas.project.RailsProjectChangedDelta;

public class RailsProjectChangedDeltaBuilder {
    
    private final IRailsProject project;
    private ControllersFolderDelta controllersFolderDelta;
    
    public RailsProjectChangedDeltaBuilder(IRailsProject project) {
        this.project = project;
    }
    
    public void setControllersFolderDelta(ControllersFolderDelta controllersFolderDelta) {
        this.controllersFolderDelta = controllersFolderDelta;
    }
    
    public RailsProjectChangedDelta build() {
        return new RailsProjectChangedDelta(project, controllersFolderDelta);
    }
    
}
