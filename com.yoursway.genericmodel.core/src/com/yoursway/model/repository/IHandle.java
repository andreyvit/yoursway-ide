package com.yoursway.model.repository;

public interface IHandle<V> {
    
    V resolve(ISnapshot snapshot);
    
    Class<? extends IModelRoot> getModelRootInterface();
    
    String toString();
    
}
