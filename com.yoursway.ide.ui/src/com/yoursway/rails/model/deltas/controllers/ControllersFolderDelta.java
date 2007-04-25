package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsControllersFolder;

public abstract class ControllersFolderDelta {
    
    private final IRailsControllersFolder folder;
    
    public ControllersFolderDelta(IRailsControllersFolder folder) {
        this.folder = folder;
    }
    
    public IRailsControllersFolder getFolder() {
        return folder;
    }
    
    public abstract void accept(IControllersFolderDeltaVisitor visitor);
    
}
