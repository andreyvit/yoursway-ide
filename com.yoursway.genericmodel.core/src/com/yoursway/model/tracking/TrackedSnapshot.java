package com.yoursway.model.tracking;

import java.util.HashMap;
import java.util.Map;

import com.yoursway.model.repository.IHandle;

public abstract class TrackedSnapshot implements IMapSnapshot {
    
    private final Map<IHandle<?>, Object> values = new HashMap<IHandle<?>, Object>();
    
    public TrackedSnapshot() {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.yoursway.model.tracking.IMapSnapshot#get(com.yoursway.model.repository.IHandle)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(IHandle<T> handle) {
        return (T) values.get(handle);
    }
    
    public <T> void put(IHandle<T> handle, T value) {
        values.put(handle, value);
    }
    
}
