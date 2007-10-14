package com.yoursway.rails.model.layer1.models;

import com.yoursway.model.repository.IHandle;

public interface ISnapshot {
    
    <V, H extends IHandle<V>> V get(H handle);
    
}
