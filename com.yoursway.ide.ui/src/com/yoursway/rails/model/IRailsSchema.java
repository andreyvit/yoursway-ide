package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsSchema extends IRailsElement {
    
    boolean hasItems();
    
    Collection<? extends IRailsTable> getItems();
    
    IRailsTable findByName(String name);
    
    int getVersion();
    
}
