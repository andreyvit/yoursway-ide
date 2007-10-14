package com.yoursway.model.repository;

import java.util.Set;

import com.yoursway.rails.model.layer1.timeline.PointInTime;

public interface BasicModelTrackerMaster {
    
    PointInTime createPointInTime();

    void handlesChanged(PointInTime moment, Set<IHandle<?>> handles);
    
}
