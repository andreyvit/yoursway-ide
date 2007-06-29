/**
 * 
 */
package com.yoursway.rails.search;

import java.util.HashMap;
import java.util.Map;

class SessionTracker implements IFamiltyTrackerParent {
    
    private boolean started;
    
    private boolean active;
    
    private double percentage;
    
    private final Map<BlockingJobFamily, FamilyTracker> familyTrackers = new HashMap<BlockingJobFamily, FamilyTracker>();
    
    private final ISessionTrackerParent parent;
    
    public SessionTracker(ISessionTrackerParent parent) {
        this.parent = parent;
    }
    
    public FamilyTracker getFamilyTracker(BlockingJobFamily family) {
        FamilyTracker familyTracker = familyTrackers.get(family);
        if (familyTracker == null) {
            familyTracker = new FamilyTracker(this, family);
            familyTrackers.put(family, familyTracker);
        }
        return familyTracker;
    }
    
    public void started() {
        if (started)
            return;
        started = true;
        parent.searchingStateChanged();
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public void searchingStateChanged() {
        active = calculateActive();
        if (!active)
            for (FamilyTracker tracker : familyTrackers.values())
                tracker.resetCompleted();
        percentage = calculatePercentage();
        
        parent.searchingStateChanged();
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    private double calculatePercentage() {
        double weightedPercentage = 0;
        for (FamilyTracker tracker : familyTrackers.values())
            weightedPercentage += tracker.getPercentage() * tracker.getFamily().getRelativeWeight();
        return weightedPercentage;
    }
    
    public boolean isActive() {
        return active;
    }
    
    private boolean calculateActive() {
        for (FamilyTracker tracker : familyTrackers.values())
            if (tracker.isActive())
                return true;
        return false;
    }
    
}