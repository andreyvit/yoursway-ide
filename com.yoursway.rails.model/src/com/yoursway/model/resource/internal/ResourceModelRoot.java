package com.yoursway.model.resource.internal;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceModelRoot;
import com.yoursway.model.resource.IResourceProject;

public class ResourceModelRoot implements IResourceModelRoot {
    
    public IHandle<Collection<IResourceProject>> projects() {
        return null;
    }
    
}
