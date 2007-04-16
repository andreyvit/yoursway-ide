package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsControllerActionsCollection extends IRailsElement {
    
    Collection<? extends IRailsAction> getActions();
    
    boolean hasItems();
    
}
