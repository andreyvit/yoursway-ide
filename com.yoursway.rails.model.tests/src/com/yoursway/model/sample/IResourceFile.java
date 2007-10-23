package com.yoursway.model.sample;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IResourceFile extends IModelElement {
    
    IHandle<String> getName();
    
    IHandle<IAST> ast();
    
}
