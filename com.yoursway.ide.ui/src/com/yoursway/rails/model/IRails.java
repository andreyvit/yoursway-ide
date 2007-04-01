package com.yoursway.rails.model;

public interface IRails {
    
    IRailsProjectsCollection getProjectsCollection();
    
    void addChangeListener(IRailsChangeListener listener);
    
    void removeChangeListener(IRailsChangeListener listener);
    
    void refresh();
    
}
