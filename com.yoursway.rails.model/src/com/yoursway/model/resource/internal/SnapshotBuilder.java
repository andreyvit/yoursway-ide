package com.yoursway.model.resource.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;
import com.yoursway.model.tracking.CompleteTrackedSnapshot;
import com.yoursway.model.tracking.TrackedSnapshot;

public class SnapshotBuilder {
    
    private final TrackedSnapshot snapshot = new CompleteTrackedSnapshot();
    
    private final Set<IHandle<?>> changedHandles = new HashSet<IHandle<?>>();
    
    private final Set<IModelElement> addedElements = new HashSet<IModelElement>();
    
    public <T> void put(IHandle<T> handle, T value) {
        snapshot.put(handle, value);
        changedHandles.add(handle);
    }
    
    public void added(IModelElement element) {
        addedElements.add(element);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.yoursway.model.resource.internal.ISnapshotBuilder#getSnapshot()
     */
    public TrackedSnapshot getSnapshot() {
        return snapshot;
    }
    
    public Set<IHandle<?>> getChangedHandles() {
        return changedHandles;
    }
    
    public Set<IModelElement> getAddedElements() {
        return addedElements;
    }
    
    public Set<IModelElement> getRemovedElements() {
        return Collections.<IModelElement> emptySet();
    }
    
}
