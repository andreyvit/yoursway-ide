package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsController;

public abstract class ControllerDelta {
    
    private final IRailsController controller;
    
    public ControllerDelta(IRailsController controller) {
        this.controller = controller;
    }
    
    public IRailsController getRailsController() {
        return controller;
    }
    
    public abstract void accept(IControllersDeltaVisitor visitor);
    
}
