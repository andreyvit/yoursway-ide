package com.yoursway.rails.model;

import java.util.Collection;

import org.eclipse.core.resources.IFolder;

public interface IRailsModelsCollection extends IProvidesRailsProject {
    
    boolean isEmpty();
    
    IFolder getModelsFolder();
    
    Collection<? extends IRailsModel> getItems();
    
}
