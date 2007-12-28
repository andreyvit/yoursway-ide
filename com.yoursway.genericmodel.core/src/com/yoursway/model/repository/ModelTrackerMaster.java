package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public interface ModelTrackerMaster {
    
    PointInTime createPointInTime();
    
    void handlesChanged(PointInTime moment, ModelDelta delta);
    
    public ISnapshotStorage getSnapshotStorage();
    
}
