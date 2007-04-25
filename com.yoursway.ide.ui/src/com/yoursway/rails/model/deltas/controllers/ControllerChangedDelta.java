package com.yoursway.rails.model.deltas.controllers;

import com.yoursway.rails.model.IRailsController;

public class ControllerChangedDelta extends ControllerDelta {
    
    public ControllerChangedDelta(IRailsController controller) {
        super(controller);
    }
    
    @Override
    public void accept(IControllersDeltaVisitor visitor) {
        visitor.visitChangedController(this);
    }
    
}
