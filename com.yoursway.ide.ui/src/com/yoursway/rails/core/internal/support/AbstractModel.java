package com.yoursway.rails.core.internal.support;

import com.yoursway.common.TypedListenerList;

public abstract class AbstractModel<Listener> {
    
    private final TypedListenerList<Listener> listeners = new TypedListenerList<Listener>();
    
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    protected abstract Listener[] makeListenersArray(int size);
    
    protected Iterable<Listener> getListeners() {
        return listeners;
    }
}
