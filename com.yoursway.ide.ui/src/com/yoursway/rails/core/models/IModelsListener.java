package com.yoursway.rails.core.models;

public interface IModelsListener {
    
    void modelAdded(RailsModel railsModel);
    
    void modelRemoved(RailsModel railsModel);
    
    void modelContentChanged(RailsModel railsModel);
    
}
