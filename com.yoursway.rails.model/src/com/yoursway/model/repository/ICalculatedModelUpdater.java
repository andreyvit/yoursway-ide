package com.yoursway.model.repository;

import java.util.Set;

import com.yoursway.model.resource.internal.SnapshotBuilder;

public interface ICalculatedModelUpdater {
    
    void update(IResolver resolver, SnapshotBuilder snapshotBuilder, Set<IHandle<?>> changedHandles);
    
    Class<?> getModelRootInterface();
    
}
