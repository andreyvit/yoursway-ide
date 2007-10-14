package com.yoursway.rails.model.layer1.calculated;

import com.yoursway.rails.model.layer1.models.IModelInstance;
import com.yoursway.rails.model.layer1.models.ISnapshot;
import com.yoursway.rails.model.layer1.timeline.PointInTime;

public interface ISnapshotResolver {

    ISnapshot provideSnapshot(IModelInstance instace, PointInTime moment);
    
}
