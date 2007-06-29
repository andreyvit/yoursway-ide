/**
 * 
 */
package com.yoursway.rails.search;

import com.yoursway.utils.PercentageProgressListener;

final class JobTracker implements PercentageProgressListener {
    
    private final BlockingJobFamily family;
    private final IJobTrackerParent parent;
    private boolean completed;
    
    public JobTracker(IJobTrackerParent parent, BlockingJobFamily family) {
        this.parent = parent;
        this.family = family;
    }
    
    private double lastPercentage = 0.0;
    
    public void completed(boolean canceled) {
        lastPercentage = 100;
        completed = true;
        parent.clientCompleted(this);
    }
    
    public void percentageUpdated(double percentDone) {
        lastPercentage += percentDone;
        parent.searchingStateChanged();
    }
    
    public void started() {
        parent.clientAdded(this);
    }
    
    public double getPercentage() {
        return lastPercentage;
    }
    
    public boolean isActive() {
        return !completed;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " (family " + family + ")";
    }
    
}