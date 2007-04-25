package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsController;

public class ControllerRemovedDelta extends ControllerDelta {
    
    public ControllerRemovedDelta(IRailsController controller) {
        super(controller);
    }
    
    @Override
    public void accept(IControllersDeltaVisitor visitor) {
        visitor.visitRemovedController(this);
    }
    
}
