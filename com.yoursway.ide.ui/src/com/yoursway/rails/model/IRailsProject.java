package com.yoursway.rails.model;

import org.eclipse.core.resources.IProject;

import com.yoursway.utils.Inflector;

public interface IRailsProject extends IRailsElement, IProvidesRailsProject {
    
    IProject getProject();
    
    IRailsControllersManager getControllersCollection();
    
    IRailsModelsCollection getModelsCollection();
    
    IRailsSchema getSchema();
    
    Inflector getInflector();
    
}
