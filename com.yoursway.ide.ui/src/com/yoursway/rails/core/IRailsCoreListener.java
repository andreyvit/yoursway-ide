package com.yoursway.rails.core;

import org.eclipse.core.resources.IResourceDelta;

public interface IRailsCoreListener {
    
    void reconcile(IResourceDelta workspaceResourceDelta);
    
}
