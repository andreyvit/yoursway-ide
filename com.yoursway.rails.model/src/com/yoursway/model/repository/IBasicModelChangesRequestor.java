package com.yoursway.model.repository;


public interface IBasicModelChangesRequestor {
    
    void theGivenPieceOfShitChanged(ISnapshot snapshot, BasicModelDelta delta);
    
}
