package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public interface IDependant {
    
    void call(ISnapshotStorage snapshotStorage, PointInTime moment, ModelDelta delta);
    
}
