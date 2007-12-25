package com.yoursway.model.repository;

import java.util.concurrent.ExecutorService;

import com.yoursway.model.resource.internal.ISnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;

public class BasicModelTracker implements IBasicModelChangesRequestor {
    
    private final Class<?> rootHandleInterface;
    private final Object rootHandle;
    private final ModelTrackerMaster master;
    private final ExecutorService executor;
    
    public BasicModelTracker(Class<?> rootHandleInterface, Object rootHandle, ModelTrackerMaster master,
            ExecutorService executor) {
        this.rootHandleInterface = rootHandleInterface;
        this.rootHandle = rootHandle;
        this.master = master;
        this.executor = executor;
    }
    
    public Class<?> getRootHandleInterface() {
        return rootHandleInterface;
    }
    
    public Object getRootHandle() {
        return rootHandle;
    }
    
    public void modelChanged(final ISnapshot snapshot, final ModelDelta delta) {
        final PointInTime moment = master.createPointInTime();
        executor.execute(new Runnable() {
            
            public void run() {
                master.getSnapshotStorage().pushSnapshot(rootHandleInterface, moment, new ISnapshotBuilder() {
                    
                    public ISnapshot buildSnapshot() {
                        return snapshot;
                    }
                    
                });
                master.handlesChanged(moment, delta);
                System.out.println("BM.run() ch=" + delta.getChangedHandles());
            }
            
        });
        
    }
    
}