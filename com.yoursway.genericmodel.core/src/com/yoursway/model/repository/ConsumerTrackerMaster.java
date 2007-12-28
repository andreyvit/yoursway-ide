package com.yoursway.model.repository;

public interface ConsumerTrackerMaster extends IModelRootProvider {
    
    void clearDependencies(IDependant dependant);
    
    void addDependency(IDependant dependant, IHandle<?> handle);
    
}
