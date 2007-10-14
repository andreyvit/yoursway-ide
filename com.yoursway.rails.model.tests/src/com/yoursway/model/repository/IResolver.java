package com.yoursway.model.repository;


public interface IResolver {
    
    <V> V obtain(Class<V> rootHandleInterface);

    <V, H extends IHandle<V>> V get(H handle);
    
    void checkCancellation();
    
}
