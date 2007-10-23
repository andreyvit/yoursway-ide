package com.yoursway.model.repository;

import java.util.Set;

public class BasicModelDelta {
    
    private Set<IHandle<?>> changedHandles;
    
    private Set<IModelElement> addedElements;
    
    private Set<IModelElement> removedElements;
    
    public BasicModelDelta(Set<IHandle<?>> changedHandles, Set<IModelElement> addedElements,
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