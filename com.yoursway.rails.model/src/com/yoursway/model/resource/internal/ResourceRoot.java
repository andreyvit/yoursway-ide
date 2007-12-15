package com.yoursway.model.resource.internal;

import java.util.Collection;

import org.eclipse.core.resources.IProject;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceModelRoot;
import com.yoursway.model.resource.IResourceProject;

public class ResourceRoot implements IResourceModelRoot {
    
    private final ResourceProjectsHandle resourceProjectsHandle = new ResourceProjectsHandle();
    
    public IHandle<Collection<IResourceProject>> projects() {
        return resourceProjectsHandle;
    }
    
    public IHandle<IResourceProject> project(IProject eclipseProject) {
        return new ResourceProjectHandle(eclipseProject);
    }
    
}
