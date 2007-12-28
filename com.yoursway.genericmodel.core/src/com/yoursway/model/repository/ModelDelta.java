package com.yoursway.model.repository;

import java.util.Collections;
import java.util.Set;

public class ModelDelta {
    
    public final static ModelDelta EMPTY_DELTA = new ModelDelta(Collections.<IHandle<?>> emptySet(),
            Collections.<IModelElement> emptySet(), Collections.<IModelElement> emptySet());
    
    private final Set<IHandle<?>> changedHandles;
    
    private final Set<IModelElement> addedElements;
    
    private final Set<IModelElement> removedElements;
    
    public ModelDelta(Set<IHandle<?>> changedHandles, Set<IModelElement> addedElements,
            Set<IModelElement> removedElements) {
        this.changedHandles = changedHandles;
        this.addedElements = addedElements;
        this.removedElements = removedElements;
    }
    
    public ModelDelta(Set<IHandle<?>> changedHandles) {
        this.changedHandles = changedHandles;
        this.addedElements = Collections.<IModelElement> emptySet();
        this.removedElements = Collections.<IModelElement> emptySet();
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