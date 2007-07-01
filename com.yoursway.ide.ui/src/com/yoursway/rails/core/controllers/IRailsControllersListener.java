package com.yoursway.rails.core.controllers;

public interface IRailsControllersListener {
    
    void controllerAdded(RailsController railsController);
    
    void controllerRemoved(RailsController railsController);
    
    void reconcile(RailsController railsController);
    
}
