package com.yoursway.rails.model.tests;

import com.yoursway.rails.model.layer1.models.IModelInstance;
import com.yoursway.rails.model.layer1.models.ISnapshot;

public interface IModelListener {
    
    void snapshotReady(IModelInstance instance, ISnapshot snapshot);
    
}
