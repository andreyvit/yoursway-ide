package com.yoursway.model.resource;

import java.io.File;

import com.yoursway.model.repository.IHandle;

public interface IResourceProject extends IResourceContainer {
    
    IHandle<String> getName();
    
    IHandle<File> getFileSystemLocation();
    
}
