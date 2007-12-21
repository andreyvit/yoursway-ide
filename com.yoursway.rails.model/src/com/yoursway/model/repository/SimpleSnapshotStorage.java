package com.yoursway.model.repository;

import java.util.HashMap;
import java.util.Map;

import com.yoursway.model.resource.internal.ISnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;
import com.yoursway.model.tracking.IMapSnapshot;

public class SimpleSnapshotStorage implements ISnapshotStorage {
    
    private final Map<Class<?>, PerModelStorage> storages = new HashMap<Class<?>, PerModelStorage>();
    
    public SimpleSnapshotStorage() {
    }
    
    public IMapSnapshot getLast(Class<?> modelRoot, PointInTime point) {
        PerModelStorage perModelStorage = storages.get(modelRoot);
        if (perModelStorage == null)
            return null;
        return perModelStorage.getLast(point);
    }
    
    public IMapSnapshot getLastAccessible(Class<?> modelRoot, PointInTime point) {
        PerModelStorage perModelStorage = storages.get(modelRoot);
        if (perModelStorage == null)
            return null;
        return perModelStorage.getLastAccessible(point);
    }
    
    public void pushSnapshot(Class<?> modelRoot, PointInTime point, ISnapshotBuilder snapshotBuilder) {
        PerModelStorage perModelStorage = storages.get(modelRoot);
        if (perModelStorage == null) {
            perModelStorage = new PerModelStorage(modelRoot);
            storages.put(modelRoot, perModelStorage);
        }
        perModelStorage.pushSnapshot(point, snapshotBuilder);
    }
    
    public void disposeResolver(IResolver resolver) {
        // TODO Auto-generated method stub
    }
    
    public void registerResolver(IResolver resolver, PointInTime point) {
        // TODO Auto-generated method stub
    }
    
}
