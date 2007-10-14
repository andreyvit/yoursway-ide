package com.yoursway.model.repository;

import com.yoursway.rails.model.layer1.models.IModelInstance;

public interface IHandle<V> {
    
    IModelInstance getModelInstance();
    
    String toString();
    
}
