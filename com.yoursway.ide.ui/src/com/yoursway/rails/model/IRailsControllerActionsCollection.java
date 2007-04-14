package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsControllerActionsCollection {
    
    Collection<? extends IRailsAction> getActions();
    
    boolean hasItems();
    
}
