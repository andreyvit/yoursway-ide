package com.yoursway.rails.model;

import org.eclipse.core.resources.IProject;

public interface IRailsProject {
    
    IProject getProject();
    
    IRailsControllersCollection getControllersCollection();
    
}
