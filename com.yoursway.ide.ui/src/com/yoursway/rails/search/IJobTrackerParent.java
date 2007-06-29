package com.yoursway.rails.search;

public interface IJobTrackerParent extends IRailsSearchingStateChangedListener {
    
    void clientAdded(JobTracker tracker);
    
    void clientCompleted(JobTracker tracker);
    
}
