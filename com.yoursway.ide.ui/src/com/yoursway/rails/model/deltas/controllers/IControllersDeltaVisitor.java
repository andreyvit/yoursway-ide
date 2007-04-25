package com.yoursway.rails.model.deltas.controllers;

public interface IControllersDeltaVisitor {
    
    void visitAddedController(ControllerAddedDelta delta);
    
    void visitRemovedController(ControllerRemovedDelta delta);
    
    void visitChangedController(ControllerChangedDelta delta);
    
}
