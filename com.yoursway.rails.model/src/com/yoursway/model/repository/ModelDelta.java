package com.yoursway.model.repository;

import java.util.Set;

public class ModelDelta {
    
    private final Set<IHandle<?>> changedHandles;
    
    private final Set<IModelElement> addedElements;
    
    private final Set<IModelElement> removedElements;
    
    public ModelDelta(Set<IHandle<?>> changedHandles, Set<IModelElement> addedElements,
            Set<IModelElement> removedElements) {
        this.changedHandles = changedHandles;
        this.addedElements = addedElements;
        this.removedElements = removedElements;
    }
    
    public Set<IHandle<?>> getChangedHandles() {
        return changedHandles;
    }
    
    public Set<IModelElement> getAddedElements() {
        return addedElements;
    }
    
    public Set<IModelElement> getRemovedElements() {
        return removedElements;
    }
    
}