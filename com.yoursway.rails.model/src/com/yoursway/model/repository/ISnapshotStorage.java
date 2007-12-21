package com.yoursway.model.repository;

import com.yoursway.model.resource.internal.ISnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;
import com.yoursway.model.tracking.IMapSnapshot;

public interface ISnapshotStorage extends IResolverTracker {
    
    IMapSnapshot getLast(Class<?> modelRoot, PointInTime point);
    
    IMapSnapshot getLastAccessible(Class<?> modelRoot, PointInTime point);
    
    void pushSnapshot(Class<?> modelRoot, PointInTime point, ISnapshotBuilder snapshotBuilder);
    
}
