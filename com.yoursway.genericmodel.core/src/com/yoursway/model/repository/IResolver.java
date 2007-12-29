package com.yoursway.model.repository;

import java.util.Collection;

/**
 * The implementations must be safe for multitheaded usage.
 */
public interface IResolver extends IModelRootProvider {
    
    <V, H extends IHandle<V>> V get(H handle) throws NoSuchHandleException;
    
    <V, H extends IHandle<V>> V getIfAvail(H handle) throws NoSuchHandleException;
    
    Collection<? extends IHandle<?>> changedHandlesForModel(Class<?> rootHandleInterface);
    
    void checkCancellation();
    
    void dontKillForLaterAccess();
    
}
