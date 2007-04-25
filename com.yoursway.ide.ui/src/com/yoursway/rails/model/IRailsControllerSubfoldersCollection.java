package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsControllerSubfoldersCollection {
    
    boolean hasSubfolders();
    
    Collection<? extends IRailsControllersFolder> getSubfolders();
    
}
