package com.yoursway.rails.model;

public interface IRailsTable extends IRailsElement {
    
    String getName();
    
    IRailsFieldsCollection getFields();
    
}
