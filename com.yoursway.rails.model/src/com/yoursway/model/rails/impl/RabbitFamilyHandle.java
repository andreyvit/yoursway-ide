package com.yoursway.model.rails.impl;

import java.util.Collection;

import com.yoursway.model.repository.ICollectionHandle;

public class RabbitFamilyHandle<T> implements ICollectionHandle<T> {
    
    private final Collection<T> t;

    public RabbitFamilyHandle(Collection<T> t) {
        this.t = t;
    }

    public Collection<T> getData() {
        return t;
    }
    
}
