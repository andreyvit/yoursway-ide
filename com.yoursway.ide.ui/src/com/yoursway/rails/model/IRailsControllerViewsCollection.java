package com.yoursway.rails.model;

import java.util.Collection;

import org.eclipse.core.resources.IFolder;

public interface IRailsControllerViewsCollection {
    
    boolean hasItems();
    
    Collection<? extends IRailsBaseView> getItems();
    
    IFolder getViewsFolder();
    
}
