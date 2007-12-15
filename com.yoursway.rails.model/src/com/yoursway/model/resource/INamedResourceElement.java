package com.yoursway.model.resource;

import com.yoursway.model.repository.IHandle;

public interface INamedResourceElement extends IResourceElement {
    
    IHandle<String> name();
    
}
