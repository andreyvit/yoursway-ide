package com.yoursway.model.repository;

import java.util.Set;

public interface ICalculatedModelUpdater {
    
    ISnapshot buildInitialSnapshot(IResolver resovler);
    
    void calculateHandle(IHandle<?> handle, IResolver resolver, ISnapshot snapshot,
            Set<IHandle<?>> updatedHandles);
    
    ISnapshot calculateHandle(IHandle<?> handle, IResolver resolver, Set<IHandle<?>> updatedHandles);
    
}
