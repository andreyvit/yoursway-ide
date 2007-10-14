package com.yoursway.rails.model.layer1.models;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.rails.model.layer1.timeline.PointInTime;
import com.yoursway.rails.model.tests.IModelListener;

public abstract class ModelFamily<S extends ISnapshot> {
    
    private Collection<IModelListener> listeners = new ArrayList<IModelListener>();
    
    public void subscribe(IModelListener listener) {
    }

    public void unsubscribe(IModelListener listener) {
    }

    public S snapshot(PointInTime now) {
        return null;
    }
    
    public abstract S buildSnapshot(PointInTime time, S previousSnapshot);
    
}
