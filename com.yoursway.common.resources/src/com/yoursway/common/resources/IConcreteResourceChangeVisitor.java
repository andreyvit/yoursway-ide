package com.yoursway.common.resources;

import org.eclipse.core.resources.IResourceDelta;

public interface IConcreteResourceChangeVisitor {

    void resourceMightHaveBeenAdded();
    
    void resourceRemovedIfExisted();
    
    void resourceChanged(IResourceDelta delta);
    
}
