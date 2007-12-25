package com.yoursway.model.repository;

import java.util.concurrent.ExecutorService;

import com.yoursway.model.resource.internal.ISnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;

public class CalculatedModelTracker implements IDependant, DependencyRequestor {
    private final Class<?> rootHandleInterface;
    private final Object rootHandle;
    private final CalculatedModelTrackerMaster master;
    private final ICalculatedModelUpdater modelUpdater;
    private final ExecutorService executor;
    private Resolver previousResolver;
    private final ISnapshotStorage storage;
    
    public CalculatedModelTracker(Class<?> rootHandleInterface, Object rootHandle,
            CalculatedModelTrackerMaster master, ICalculatedModelUpdater modelUpdater,
            ExecutorService executor, final ISnapshotStorage storage) {
        this.rootHandleInterface = rootHandleInterface;
        this.rootHandle = rootHandle;
        this.master = master;
        this.modelUpdater = modelUpdater;
        this.executor = executor;
        this.storage = storage;
        this.previousResolver = null;
    }
    
    public Class<?> getRootHandleInterface() {
        return rootHandleInterface;
    }
    
    public Object getRootHandle() {
        return rootHandle;
    }
    
    public void call(final PointInTime point, final ModelDelta delta) {
        if (previousResolver != null) {
            storage.disposeResolver(previousResolver);
            previousResolver = null;
        }
        final Resolver resolver = new Resolver(point, master, this, storage, delta);
        executor.execute(new Runnable() {
            
            @SuppressWarnings("unchecked")
            public void run() {
                master.clearDependencies(CalculatedModelTracker.this);
                final ModelDelta[] newDelta = new ModelDelta[1];
                storage.pushSnapshot(rootHandleInterface, point, new ISnapshotBuilder() {
                    
                    public ISnapshot buildSnapshot() {
                        SnapshotDeltaPair update = modelUpdater.update(resolver);
                        newDelta[0] = update.getDelta();
                        return update.getSnapshot();
                    }
                    
                });
                if (resolver.inGodMode())
                    previousResolver = resolver;
                else
                    storage.disposeResolver(resolver);
                master.handlesChanged(point, newDelta[0]);
                System.out.println("CM.run() ch=" + newDelta[0].getChangedHandles());
            }
            
        });
    }
    
    public void dependency(IHandle<?> handle) {
        if (handle.getModelRootInterface() != rootHandleInterface)
            master.addDependency(this, handle);
    }
    
}
