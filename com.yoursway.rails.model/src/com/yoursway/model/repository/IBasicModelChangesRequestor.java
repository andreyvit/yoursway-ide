package com.yoursway.model.repository;

public interface IBasicModelChangesRequestor {
    
    void modelChanged(ISnapshot snapshot, BasicModelDelta delta);
    
}
