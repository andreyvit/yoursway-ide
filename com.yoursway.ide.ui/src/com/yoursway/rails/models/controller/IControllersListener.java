package com.yoursway.rails.models.controller;

public interface IControllersListener {
    
    void controllerAdded(RailsController railsController);
    
    void controllerRemoved(RailsController railsController);
    
    void reconcile(RailsController railsController);
    
}
