/**
 * 
 */
package com.yoursway.rails.search;

import java.util.ArrayList;
import java.util.Collection;


class FamilyTracker implements IJobTrackerParent {
    
    private final Collection<JobTracker> trackers = new ArrayList<JobTracker>();
    
    private int completedJobs = 0;
    
    private double percentage;
    
    private boolean active;
    
    private final IFamiltyTrackerParent parent;
    
    private final BlockingJobFamily family;
    
    public FamilyTracker(IFamiltyTrackerParent parent, BlockingJobFamily family) {
        this.parent = parent;
        this.family = family;
    }
    
    public synchronized void clientAdded(JobTracker tracker) {
        trackers.add(tracker);
        searchingStateChanged();
    }
    
    public synchronized void clientCompleted(JobTracker tracker) {
        trackers.remove(tracker);
        completedJobs++;
        searchingStateChanged();
    }
    
    public BlockingJobFamily getFamily() {
        return family;
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void resetCompleted() {
        completedJobs = 0;
    }
    
    public synchronized void searchingStateChanged() {
        active = calculateActive();
        percentage = calculatePercentage();
        parent.searchingStateChanged();
    }
    
    private boolean calculateActive() {
        for (JobTracker tracker : trackers)
            if (tracker.isActive())
                return true;
        return false;
    }
    
    private double calculatePercentage() {
        double totalPercentage = (completedJobs + trackers.size()) * 100.0;
        double currentPercentage = completedJobs * 100.0;
        for (JobTracker tracker : trackers)
            currentPercentage += tracker.getPercentage();
        return currentPercentage / totalPercentage * 100.0;
    }
    
}