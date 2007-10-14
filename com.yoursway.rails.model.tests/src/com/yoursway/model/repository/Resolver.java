/**
 * 
 */
package com.yoursway.model.repository;

import java.util.Collection;

import com.yoursway.rails.model.layer1.timeline.PointInTime;

public final class Resolver implements IResolver {
    
    private final PointInTime moment;
    private final DependencyRequestor dependencyRequestor;
    private final IRootHandleProvider rootHandleProvider;
    
    public Resolver(PointInTime moment, IRootHandleProvider rootHandleProvider,
            DependencyRequestor dependencyRequestor) {
        this.moment = moment;
        this.rootHandleProvider = rootHandleProvider;
        this.dependencyRequestor = dependencyRequestor;
    }
    
    public void checkCancellation() {
    }
    
    public <V, H extends IHandle<V>> V get(H handle) {
        dependencyRequestor.dependency(handle);
        return null;
    }
    
    public <V> V obtain(Class<V> rootHandleInterface) {
        return rootHandleProvider.obtain(rootHandleInterface);
    }
    
    public <V> Collection<? extends V> changedHandles(Class<V> handleInterface) {
        return null;
    }
    
}