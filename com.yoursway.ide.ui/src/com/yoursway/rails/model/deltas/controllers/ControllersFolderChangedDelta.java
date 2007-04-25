package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsControllersFolder;

public class ControllersFolderChangedDelta extends ControllersFolderDelta {
    
    private final ControllersFolderDelta[] subfolderDeltas;
    private final ControllerDelta[] controllerDeltas;
    
    public ControllersFolderChangedDelta(IRailsControllersFolder folder,
            ControllersFolderDelta[] subfolderDeltas, ControllerDelta[] controllerDeltas) {
        super(folder);
        this.subfolderDeltas = subfolderDeltas;
        this.controllerDeltas = controllerDeltas;
    }
    
    @Override
    public void accept(IControllersFolderDeltaVisitor visitor) {
        visitor.visitChangedFolder(this);
    }
    
    public ControllerDelta[] getControllerDeltas() {
        return controllerDeltas;
    }
    
    public ControllersFolderDelta[] getSubfolderDeltas() {
        return subfolderDeltas;
    }
    
}
