package com.yoursway.model.resource;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;

public interface IResourceModelRoot extends IModelRoot {
    
    IHandle<Collection<IResourceProject>> projects();
    
}
