package com.yoursway.model.repository;

public interface ConsumerTrackerMaster extends IModelRootProvider {
    
    void addDependency(IDependant dependant, IHandle<?> handle);
    
}
