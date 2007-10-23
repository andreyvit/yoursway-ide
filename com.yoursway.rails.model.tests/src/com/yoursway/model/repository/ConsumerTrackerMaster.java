package com.yoursway.model.repository;

public interface ConsumerTrackerMaster extends IModelRootProvider {
    
    void addDependency(ConsumerTracker consumerTracker, IHandle<?> handle);
    
}
