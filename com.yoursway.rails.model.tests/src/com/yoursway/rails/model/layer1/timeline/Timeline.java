package com.yoursway.rails.model.layer1.timeline;

import java.util.Collection;

import com.yoursway.rails.model.layer1.models.ModelFamily;

public class Timeline {
    
    private volatile PointInTime now;
    
    private int moment = 1;
    
    public Timeline(Collection<ModelFamily<?>> basicFamilies) {
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
