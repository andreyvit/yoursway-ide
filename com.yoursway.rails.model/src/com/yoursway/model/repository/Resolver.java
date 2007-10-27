/**
 * 
 */
package com.yoursway.model.repository;

import java.util.Collection;

import com.yoursway.model.timeline.PointInTime;

public final class Resolver implements IResolver {
    
    private final PointInTime moment;
    private final DependencyRequestor dependencyRequestor;
    private final IModelRootProvider modelRootProvider;
    
    public Resolver(PointInTime moment, IModelRootProvider modelRootProvider,
            DependencyRequestor dependencyRequestor) {
        this.moment = moment;
        this.modelRootProvider = modelRootProvider;
        this.dependencyRequestor = dependencyRequestor;
    }
    
    public void checkCancellation() {
    }
    
    public <V, H extends IHandle<V>> V get(H handle) {
        dependencyRequestor.dependency(handle);
        return null;
    }
    
    public <V extends IModelRoot> V obtainRoot(Class<V> rootHandleInterface) {
        return modelRootProvider.obtainRoot(rootHandleInterface);
    }
    
    public <V> Collection<? extends V> changedHandles(Class<V> handleInterface) {
        return null;
    }

    public <V, H extends IHandle<V>> V getIfAvail(H handle) {
        return null;
    }
    
}