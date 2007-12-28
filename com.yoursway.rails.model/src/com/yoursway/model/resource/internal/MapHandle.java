package com.yoursway.model.resource.internal;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.ISnapshot;
import com.yoursway.model.tracking.IMapSnapshot;

public abstract class MapHandle<T> implements IHandle<T> {
    
    public T resolve(ISnapshot snapshot) {
        if (snapshot instanceof IMapSnapshot) {
            IMapSnapshot mapSnapshot = (IMapSnapshot) snapshot;
            return mapSnapshot.get(this);
        }
        return null;
    }
    
}
