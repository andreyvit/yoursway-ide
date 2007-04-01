package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsControllersCollection {
    
    Collection<? extends IRailsController> getItems();
    
}
