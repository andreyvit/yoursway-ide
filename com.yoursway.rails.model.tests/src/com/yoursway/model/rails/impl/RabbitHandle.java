package com.yoursway.model.rails.impl;

import com.yoursway.model.repository.IHandle;

public class RabbitHandle<T> implements IHandle<T> {
    
    private final T t;

    public RabbitHandle(T t) {
        this.t = t;
    }

    public T getData() {
        return t;
    }
    
}
