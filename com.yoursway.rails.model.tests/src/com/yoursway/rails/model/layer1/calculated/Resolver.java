package com.yoursway.rails.model.layer1.calculated;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IResolver;
import com.yoursway.rails.model.layer1.models.IModelInstance;
import com.yoursway.rails.model.layer1.models.ISnapshot;
import com.yoursway.rails.model.layer1.models.ModelFamily;
import com.yoursway.rails.model.layer1.timeline.PointInTime;
import com.yoursway.rails.model.layer1.timeline.Timeline;

public final class Resolver implements IResolver {
    
    private final PointInTime moment;
    
    private Set<IModelInstance> dependencies = new HashSet<IModelInstance>();
    
    private Map<IModelInstance, ISnapshot> snapshots = new HashMap<IModelInstance, ISnapshot>();

    private final ISnapshotResolver snapshotResolver;
    
    public Resolver(ISnapshotResolver snapshotResolver, PointInTime moment) {
        this.snapshotResolver = snapshotResolver;
        this.moment = moment;
    }
    
    protected final <V> V get(ModelFamily<?> family, IHandle<V> handle) {
        dependencies.add(family);
        return null;
    }

    public void checkCancellation() {
    }

    public <V, H extends IHandle<V>> V get(H handle) {
        IModelInstance instance = handle.getModelInstance();
        ISnapshot snapshot = snapshots.get(instance);
        if (snapshot == null) {
            snapshot = snapshotResolver.provideSnapshot(instance, moment);
            snapshots.put(instance, snapshot);
        }
        return null;
    }
    
}
