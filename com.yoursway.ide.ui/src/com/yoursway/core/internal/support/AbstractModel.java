package com.yoursway.core.internal.support;

import com.yoursway.utils.TypedListenerList;

public abstract class AbstractModel<Listener> {
    
    private final TypedListenerList<Listener> listeners = new TypedListenerList<Listener>() {
        
        @Override
        protected Listener[] makeArray(int size) {
            return makeListenersArray(size);
        }
        
    };
    
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    protected abstract Listener[] makeListenersArray(int size);
    
    protected Listener[] getListeners() {
        return listeners.getListeners();
    }
    
}
