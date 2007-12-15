package com.yoursway.model.resource.internal;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public abstract class WorkspaceResourceChangeListener implements IResourceChangeListener {
    
    private final IWorkspace workspace;
    
    public WorkspaceResourceChangeListener() {
        workspace = ResourcesPlugin.getWorkspace();
        workspace.addResourceChangeListener(this);
    }
    
    protected void dispose() {
        workspace.removeResourceChangeListener(this);
    }
    
    protected final IWorkspace getWorkspace() {
        return workspace;
    }
    
    protected final IWorkspaceRoot getWorkspaceRoot() {
        return workspace.getRoot();
    }
    
}
