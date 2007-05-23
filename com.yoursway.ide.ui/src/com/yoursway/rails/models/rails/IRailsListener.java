package com.yoursway.rails.models.rails;

import org.eclipse.core.resources.IResourceDelta;

public interface IRailsListener {
    
    void reconcile(IResourceDelta workspaceResourceDelta);
    
}
