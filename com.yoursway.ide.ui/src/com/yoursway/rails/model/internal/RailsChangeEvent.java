package com.yoursway.rails.model.internal;

import com.yoursway.rails.model.IRails;
import com.yoursway.rails.model.IRailsChangeEvent;

public class RailsChangeEvent implements IRailsChangeEvent {
    
    private final Rails rails;
    
    public RailsChangeEvent(Rails rails) {
        this.rails = rails;
    }
    
    public IRails getRails() {
        return rails;
    }
    
}
