/**
 * 
 */
package com.yoursway.model.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yoursway.rails.model.layer1.timeline.PointInTime;

public class BasicModelTracker implements IBasicModelChangesRequestor {
    
    private final Class<?> rootHandleInterface;
    private final Object rootHandle;
    private final BasicModelTrackerMaster master;
    
    private Map<PointInTime, ISnapshot> snapshots = new HashMap<PointInTime, ISnapshot>();
    
    private List<PointInTime> momentsOfGlory = new ArrayList<PointInTime>();
    
    public BasicModelTracker(Class<?> rootHandleInterface, Object rootHandle, BasicModelTrackerMaster master) {
        this.rootHandleInterface = rootHandleInterface;
        this.rootHandle = rootHandle;
        this.master = master;
    }
    
    public Class<?> getRootHandleInterface() {
        return rootHandleInterface;
    }
    
    public Object getRootHandle() {
        return rootHandle;
    }
    
    public void theGivenPieceOfShitChanged(ISnapshot snapshot, BasicModelDelta delta) {
        PointInTime moment = master.createPointInTime();
        momentsOfGlory.add(moment);
        snapshots.put(moment, snapshot);
        master.handlesChanged(moment, delta);
    }
    
}