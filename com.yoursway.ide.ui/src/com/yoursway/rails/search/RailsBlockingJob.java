package com.yoursway.rails.search;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

public class RailsBlockingJob extends Job {
    
    public RailsBlockingJob(String name) {
        super(name);
    }
    
    @Override
    protected final IStatus run(IProgressMonitor monitor) {
        return null;
    }
    
}
