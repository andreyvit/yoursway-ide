package com.yoursway.model.repository;

import java.util.Collection;

public interface IResolver extends IRootHandleProvider {
    
    <V, H extends IHandle<V>> V get(H handle);
    
    <V> Collection<? extends V> changedHandles(Class<V> handleInterface);
    
    void checkCancellation();
    
}
