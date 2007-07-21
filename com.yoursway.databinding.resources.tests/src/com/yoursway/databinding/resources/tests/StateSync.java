package com.yoursway.databinding.resources.tests;

import org.eclipse.core.runtime.Assert;

public class StateSync<T> {
    
    private T state;
    
    public StateSync(T initialState) {
        Assert.isNotNull(initialState);
        state = initialState;
    }
    
    public synchronized void setState(T newState) {
        Assert.isNotNull(newState);
        state = newState;
        notifyAll();
    }
    
    public synchronized void waitState(T reqState) {
        Assert.isNotNull(reqState);
        while (!state.equals(reqState))
            try {
                wait();
            } catch (InterruptedException e) {
            }
    }
    
}
