package com.yoursway.model.sample;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IResourceProject extends IModelElement {
    
    IHandle<String> getName();
    
    ICollectionHandle<IResourceFile> files();
    
}
