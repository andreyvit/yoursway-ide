package com.yoursway.model.repository;

import java.util.Set;

public interface IBasicModelChangesRequestor {

    void theGivenPieceOfShitChanged(ISnapshot snapshot, Set<IHandle<?>> handles);
    
}
