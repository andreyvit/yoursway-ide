package com.yoursway.model.resource;

import java.util.Collection;

import org.eclipse.core.resources.IProject;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;

public interface IResourceModelRoot extends IModelRoot {
    
    IHandle<Collection<IResourceProject>> projects();
    
    IHandle<IResourceProject> project(IProject eclipseProject);
    
}
