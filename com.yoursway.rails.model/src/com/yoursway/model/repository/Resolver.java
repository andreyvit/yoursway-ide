/**
 * 
 */
package com.yoursway.model.repository;

import java.util.Collection;

import com.yoursway.model.timeline.PointInTime;
import com.yoursway.model.tracking.IMapSnapshot;

public final class Resolver implements IResolver {
    
    private final PointInTime moment;
    private final DependencyRequestor dependencyRequestor;
    private final IModelRootProvider modelRootProvider;
    private final ISnapshotStorage storage;
    
    public Resolver(PointInTime moment, IModelRootProvider modelRootProvider,
            DependencyRequestor dependencyRequestor, ISnapshotStorage storage) {
        this.moment = moment;
        this.modelRootProvider = modelRootProvider;
        this.dependencyRequestor = dependencyRequestor;
        this.storage = storage;
    }
    
    public void checkCancellation() {
    }
    
    public <V, H extends IHandle<V>> V get(H handle) throws NoSuchHandleException {
        return getFromStorage(handle, false);
    }
    
    public <V extends IModelRoot> V obtainRoot(Class<V> rootHandleInterface) {
        return modelRootProvider.obtainRoot(rootHandleInterface);
    }
    
    public <V> Collection<? extends V> changedHandles(Class<V> handleInterface) {
        // TODO
        // BTW, test me!
        return null;
    }
    
    public <V, H extends IHandle<V>> V getIfAvail(H handle) throws NoSuchHandleException {
        return getFromStorage(handle, true);
    }
    
    private <V, H extends IHandle<V>> V getFromStorage(H handle, boolean ifAvailOnly)
            throws NoSuchHandleException {
        dependencyRequestor.dependency(handle);
        Class<?> rootInterface = handle.getModelRootInterface();
        IMapSnapshot last;
        if (ifAvailOnly)
            last = storage.getLastAccessible(rootInterface, moment);
        else
            last = storage.getLast(rootInterface, moment);
        if (last == null)
            return null;
        V v = last.get(handle);
        if (v == null)
            throw new NoSuchHandleException();
        return v;
    }
    
}