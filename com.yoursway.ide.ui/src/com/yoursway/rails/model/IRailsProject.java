package com.yoursway.rails.model;

import org.eclipse.core.resources.IProject;

public interface IRailsProject extends IProvidesRailsProject {
    
    IProject getProject();
    
    IRailsControllersCollection getControllersCollection();
    
}
