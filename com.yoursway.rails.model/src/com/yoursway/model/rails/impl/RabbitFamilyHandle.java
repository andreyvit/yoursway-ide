package com.yoursway.model.rails.impl;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;

public class RabbitFamilyHandle<T> implements IHandle<Collection<T>> {
    
    private final Collection<T> t;

    public RabbitFamilyHandle(Collection<T> t) {
        this.t = t;
    }

    public Collection<T> getData() {
        return t;
    }
    
}
