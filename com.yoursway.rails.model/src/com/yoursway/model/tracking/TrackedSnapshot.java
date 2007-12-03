package com.yoursway.model.tracking;

import java.util.HashMap;
import java.util.Map;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.ISnapshot;

public abstract class TrackedSnapshot implements ISnapshot {
    
    private final Map<IHandle<?>, Object> values = new HashMap<IHandle<?>, Object>();
    
    @SuppressWarnings("unchecked")
    public <T> T get(IHandle<T> handle) {
        return (T) values.get(handle);
    }
    
    public <T> void put(IHandle<T> handle, T value) {
        values.put(handle, value);
    }
    
}
