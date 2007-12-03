package com.yoursway.model.resource.internal;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.resources.IProject;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceFile;
import com.yoursway.model.resource.IResourceFolder;
import com.yoursway.model.resource.IResourceProject;

public class ResourceProject implements IResourceProject {
    
    public IHandle<String> name() {
        return new AbstractProjectHandle<String>(this) {
            
        };
    }
    
    public IHandle<File> fileSystemLocation() {
        return new AbstractProjectHandle<File>(this) {
            
        };
    }
    
    public IHandle<IProject> eclipseProject() {
        return new AbstractProjectHandle<IProject>(this) {
            
        };
    }
    
    public IHandle<Collection<IResourceFile>> files() {
        return new AbstractProjectHandle<Collection<IResourceFile>>(this) {
            
        };
    }
    
    public IHandle<Collection<IResourceFolder>> folders() {
        return new AbstractProjectHandle<Collection<IResourceFolder>>(this) {
            
        };
    }
    
}
