package com.yoursway.rails.model.layer1.int_handles;

import com.yoursway.model.repository.IHandle;
import com.yoursway.rails.model.layer1.calculated.CalculatedModelInstance;

public class Handle<V, M extends CalculatedModelInstance> implements IHandle<V> {
    
    private final CalculatedModelInstance instance;
    private final int ordinal;

    public Handle(CalculatedModelInstance instance, int ordinal) {
        this.instance = instance;
        this.ordinal = ordinal;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
}
