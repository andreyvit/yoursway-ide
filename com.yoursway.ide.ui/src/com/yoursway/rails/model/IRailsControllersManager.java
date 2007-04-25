package com.yoursway.rails.model;


public interface IRailsControllersManager extends IRailsElement, IProvidesRailsProject {
    
    IRailsControllersFolder getRootFolder();
    
    IRailsController getApplicationController();
    
}
