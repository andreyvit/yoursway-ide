package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsControllersFolder;

public class ControllersFolderAddedDelta extends ControllersFolderDelta {
    
    public ControllersFolderAddedDelta(IRailsControllersFolder folder) {
        super(folder);
    }
    
    @Override
    public void accept(IControllersFolderDeltaVisitor visitor) {
        visitor.visitAddedFolder(this);
    }
    
}
