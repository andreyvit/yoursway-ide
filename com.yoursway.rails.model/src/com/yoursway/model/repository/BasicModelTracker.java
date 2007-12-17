package com.yoursway.model.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.yoursway.model.timeline.PointInTime;

public class BasicModelTracker implements IBasicModelChangesRequestor {
    
    private final Class<?> rootHandleInterface;
    private final Object rootHandle;
    private final BasicModelTrackerMaster master;
    
    private final Map<PointInTime, ISnapshot> snapshots = new HashMap<PointInTime, ISnapshot>();
    
    private final List<PointInTime> momentsOfGlory = new ArrayList<PointInTime>();
    
    //    private final ExecutorService executor;
    
    public BasicModelTracker(Class<?> rootHandleInterface, Object rootHandle, BasicModelTrackerMaster master,
            ExecutorService executor) {
        this.rootHandleInterface = rootHandleInterface;
        this.rootHandle = rootHandle;
        this.master = master;
        //        this.executor = executor;
    }
    
    public Class<?> getRootHandleInterface() {
        return rootHandleInterface;
    }
    
    public Object getRootHandle() {
        return rootHandle;
    }
    
    public void modelChanged(final ISnapshot snapshot, final BasicModelDelta delta) {
        final PointInTime moment = master.createPointInTime();
        //        executor.execute(new Runnable() {
        //            
        //            public void run() {
        momentsOfGlory.add(moment);
        snapshots.put(moment, snapshot);
        master.handlesChanged(moment, delta);
        //            }
        //            
        //        });
    }
    
}