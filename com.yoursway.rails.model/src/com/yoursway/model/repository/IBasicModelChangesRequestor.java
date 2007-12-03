package com.yoursway.model.repository;

import java.util.concurrent.Executor;

public interface IBasicModelChangesRequestor extends Executor {
    
    void theGivenPieceOfShitChanged(ISnapshot snapshot, BasicModelDelta delta);
    
}
