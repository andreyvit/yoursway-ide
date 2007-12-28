package com.yoursway.model.repository;


public interface IBasicModelChangesRequestor {
    
    void modelChanged(ISnapshot snapshot, ModelDelta delta);
    
    void execute(Runnable r);
    
}
