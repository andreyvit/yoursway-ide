package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public interface BasicModelTrackerMaster {
    
    PointInTime createPointInTime();

    void handlesChanged(PointInTime moment, BasicModelDelta delta);
    
}
