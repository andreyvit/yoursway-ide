package com.yoursway.model.resource;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;

public interface IResourceContainer extends IResourceElement {
    
    IHandle<Collection<IResourceFile>> files();
    
    IHandle<Collection<IResourceFolder>> folders();
    
}
