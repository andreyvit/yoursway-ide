package com.yoursway.model.resource.internal;

import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.resource.IResourceModelRoot;

public class ResourceHandle<T> extends MapHandle<T> {
    
    public Class<? extends IModelRoot> getModelRootInterface() {
        return IResourceModelRoot.class;
    }
    
}
