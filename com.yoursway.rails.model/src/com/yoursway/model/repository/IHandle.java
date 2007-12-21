package com.yoursway.model.repository;

public interface IHandle<V> {
    
    Class<?> getModelRootInterface();
    
    String toString();
    
}
