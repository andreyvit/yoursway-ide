package com.yoursway.model.repository;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

import com.yoursway.model.resource.internal.SnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;

public class CalculatedModelTracker implements IDependant, DependencyRequestor {
    private final Class<?> rootHandleInterface;
    private final Object rootHandle;
    private final CalculatedModelTrackerMaster master;
    private final ICalculatedModelUpdater modelUpdater;
    private final ExecutorService executor;
    
    public CalculatedModelTracker(Class<?> rootHandleInterface, Object rootHandle,
            CalculatedModelTrackerMaster master, ICalculatedModelUpdater modelUpdater,
            ExecutorService executor) {
        this.rootHandleInterface = rootHandleInterface;
        this.rootHandle = rootHandle;
        this.master = master;
        this.modelUpdater = modelUpdater;
        this.executor = executor;
    }
    
    public Class<?> getRootHandleInterface() {
        return rootHandleInterface;
    }
    
    public Object getRootHandle() {
        return rootHandle;
    }
    
    public void call(final ISnapshotStorage storage, final PointInTime point, final ModelDelta delta) {
        final Resolver resolver = new Resolver(point, master, this, storage);
        storage.registerResolver(resolver, point);
        executor.execute(new Runnable() {
            
            @SuppressWarnings("unchecked")
            public void run() {
                SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
                modelUpdater.update(resolver, snapshotBuilder, ((delta == null) ? Collections.EMPTY_SET
                        : delta.getChangedHandles()));
                storage.pushSnapshot(rootHandleInterface, point, snapshotBuilder);
            }
            
        });
    }
    
    public void dependency(IHandle<?> handle) {
        master.addDependency(this, handle);
    }
    
}
