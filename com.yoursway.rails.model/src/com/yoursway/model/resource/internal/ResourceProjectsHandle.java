package com.yoursway.model.resource.internal;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceProject;

public class ResourceProjectsHandle implements IHandle<Collection<IResourceProject>> {
    
    @Override
    public String toString() {
        return "projects-collection";
    }
    
}
