package com.yoursway.rails.search;

import org.eclipse.core.runtime.jobs.Job;

public interface IRailsSearchingListener extends IRailsSearchingStateChangedListener {
    
    void blockedJobAdded(Job job);
    
    void allJobsUnblocked();
    
}
