package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public interface ISnapshotStorage extends IResolverTracker {
    
    ISnapshot getLast(Class<?> modelRoot, PointInTime point);
    
    ISnapshot getLastAccessible(Class<?> modelRoot, PointInTime point);
    
    void pushSnapshot(Class<?> modelRoot, PointInTime point, ISnapshotBuilder snapshotBuilder);
    
}
