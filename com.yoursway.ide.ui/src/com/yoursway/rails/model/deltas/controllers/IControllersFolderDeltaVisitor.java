package com.yoursway.rails.model.deltas.controllers;

public interface IControllersFolderDeltaVisitor {
    
    void visitAddedFolder(ControllersFolderAddedDelta delta);
    
    void visitRemovedFolder(ControllersFolderRemovedDelta delta);
    
    void visitChangedFolder(ControllersFolderChangedDelta delta);
    
}
