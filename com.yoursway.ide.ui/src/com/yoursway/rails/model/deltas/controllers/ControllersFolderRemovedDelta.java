package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsControllersFolder;

public class ControllersFolderRemovedDelta extends ControllersFolderDelta {
    
    public ControllersFolderRemovedDelta(IRailsControllersFolder folder) {
        super(folder);
    }
    
    @Override
    public void accept(IControllersFolderDeltaVisitor visitor) {
        visitor.visitRemovedFolder(this);
    }
    
}
