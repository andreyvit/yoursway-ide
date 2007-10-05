package com.yoursway.rails.model.tests;

import java.util.Set;

public class FooSnapshotBuilder {
    
    private final PointInTime pointInTime;

    public FooSnapshotBuilder(PointInTime pointInTime) {
        this.pointInTime = pointInTime;
    }
    
    protected ISnapshot snapshot(IModelInstance instance) {
        
    }
    
}
