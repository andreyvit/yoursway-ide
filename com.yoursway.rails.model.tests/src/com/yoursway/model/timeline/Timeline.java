package com.yoursway.model.timeline;

public class Timeline {
    
    private volatile PointInTime now;
    
    private int moment = 1;
    
    public Timeline() {
        advanceThisCrazyWorldToTheNextMomentInTime();
    }
    
    public PointInTime now() {
        return now;
    }
    
    public synchronized PointInTime advanceThisCrazyWorldToTheNextMomentInTime() {
        now = new PointInTime(moment++);
        return now;
    }
    
}
