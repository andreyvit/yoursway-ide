package com.yoursway.model.resource;

import com.yoursway.model.repository.ICollectionHandle;

public interface IResourceContainer extends IResourceElement {

    ICollectionHandle<IResourceFile> files();
    
    ICollectionHandle<IResourceFolder> folders();
    
}
