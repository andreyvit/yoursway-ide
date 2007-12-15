package com.yoursway.model.resource;

import java.io.File;

import org.eclipse.core.resources.IProject;

import com.yoursway.model.repository.IHandle;

public interface IResourceProject extends IResourceContainer {
    
    IHandle<String> name();
    
    IHandle<IProject> eclipseProject();
    
    IHandle<File> fileSystemLocation();
    
}
