package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsFieldsCollection {
    
    boolean hasItems();
    
    boolean areItemsKnown();
    
    Collection<? extends IRailsField> getItems();
    
}
