package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsController;

public class ControllerAddedDelta extends ControllerDelta {
    
    public ControllerAddedDelta(IRailsController controller) {
        super(controller);
    }
    
    @Override
    public void accept(IControllersDeltaVisitor visitor) {
        visitor.visitAddedController(this);
    }
    
}
