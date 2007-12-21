package com.yoursway.model.tracking;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.ISnapshot;

public interface IMapSnapshot extends ISnapshot {
    
    public <T> T get(IHandle<T> handle);
    
}