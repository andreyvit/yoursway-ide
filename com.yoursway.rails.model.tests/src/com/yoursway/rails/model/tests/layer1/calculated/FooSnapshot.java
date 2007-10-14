package com.yoursway.rails.model.tests.layer1.calculated;

import com.yoursway.model.repository.IHandle;
import com.yoursway.rails.model.layer1.models.ISnapshot;

public class FooSnapshot implements ISnapshot {
    
    public <V, H extends IHandle<V>> V get(H handle) {
        return null;
    }
    
}
