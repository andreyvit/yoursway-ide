package com.yoursway.model.repository;

public interface ConsumerTrackerMaster extends IRootHandleProvider {
    
    void addDependency(ConsumerTracker consumerTracker, IHandle<?> handle);
    
}
