package com.yoursway.model.sample;

import com.yoursway.model.repository.IHandle;

public interface IResourceFile {
    
    String getName();
    
    IHandle<IAST> ast();
    
}
