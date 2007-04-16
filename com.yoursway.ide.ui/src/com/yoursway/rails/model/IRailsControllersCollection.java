package com.yoursway.rails.model;

import java.util.Collection;

import org.eclipse.core.resources.IFolder;

public interface IRailsControllersCollection extends IRailsElement, IProvidesRailsProject {
    
    boolean isEmpty();
    
    IFolder getControllersFolder();
    
    Collection<? extends IRailsController> getItems();
    
}
