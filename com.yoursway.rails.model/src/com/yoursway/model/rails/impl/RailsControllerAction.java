package com.yoursway.model.rails.impl;

import com.yoursway.model.rails.IRailsControllerAction;
import com.yoursway.model.repository.IHandle;

public class RailsControllerAction implements IRailsControllerAction {
    
    private final String name;
    
    public RailsControllerAction(String name) {
        this.name = name;
    }
    
    public IHandle<String> name() {
        return null;
    }
    
}
