package com.yoursway.model.repository;

public class SnapshotDeltaPair {
    
    private final ISnapshot snapshot;
    
    private final ModelDelta delta;
    
    public SnapshotDeltaPair(ISnapshot snapshot, ModelDelta delta) {
        super();
        this.snapshot = snapshot;
        this.delta = delta;
    }
    
    public ISnapshot getSnapshot() {
        return snapshot;
    }
    
    public ModelDelta getDelta() {
        return delta;
    }
    
}
