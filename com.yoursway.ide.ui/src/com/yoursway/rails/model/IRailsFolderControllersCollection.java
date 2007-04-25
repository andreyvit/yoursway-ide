package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsFolderControllersCollection {
    
    boolean hasControllers();
    
    Collection<? extends IRailsController> getControllers();
    
}
