/**
 * 
 */
package com.yoursway.model.repository;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.model.timeline.PointInTime;

public final class Resolver implements IResolver {
    
    private final PointInTime moment;
    private final DependencyRequestor dependencyRequestor;
    private final IModelRootProvider modelRootProvider;
    private final ISnapshotStorage storage;
    private final ModelDelta delta;
    private boolean godMode;
    
    public Resolver(PointInTime moment, IModelRootProvider modelRootProvider,
            DependencyRequestor dependencyRequestor, ISnapshotStorage storage, ModelDelta delta) {
        this.moment = moment;
        this.modelRootProvider = modelRootProvider;
        this.dependencyRequestor = dependencyRequestor;
        this.storage = storage;
        this.delta = delta;
        this.godMode = false;
        storage.registerResolver(this, moment);
    }
    
    public void checkCancellation() {
    }
    
    public <V, H extends IHandle<V>> V get(H handle) throws NoSuchHandleException {
        return getFromStorage(handle, false);
    }
    
    public <V extends IModelRoot> V obtainRoot(Class<V> rootHandleInterface) {
        return modelRootProvider.obtainRoot(rootHandleInterface);
    }
    
    public <V, H extends IHandle<V>> V getIfAvail(H handle) throws NoSuchHandleException {
        return getFromStorage(handle, true);
    }
    
    private <V, H extends IHandle<V>> V getFromStorage(H handle, boolean ifAvailOnly)
            throws NoSuchHandleException {
        dependencyRequestor.dependency(handle);
        Class<?> rootInterface = handle.getModelRootInterface();
        ISnapshot last;
        if (ifAvailOnly)
            last = storage.getLastAccessible(rootInterface, moment);
        else
            last = storage.getLast(rootInterface, moment);
        if (last == null)
            return null;
        V v = handle.resolve(last);
        if (v == null)
            throw new NoSuchHandleException();
        return v;
    }
    
    public Collection<? extends IHandle<?>> changedHandlesForModel(Class<?> rootHandleInterface) {
        Collection<IHandle<?>> res = new ArrayList<IHandle<?>>();
        for (IHandle<?> h : delta.getChangedHandles()) {
            if (rootHandleInterface.isAssignableFrom(h.getModelRootInterface())) {
                res.add(h);
            }
        }
        return res;
    }
    
    public void dontKillForLaterAccess() {
        this.godMode = true;
    }
    
    public boolean inGodMode() {
        return godMode;
    }
    
}