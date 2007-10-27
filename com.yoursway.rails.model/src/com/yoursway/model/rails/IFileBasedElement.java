package com.yoursway.model.rails;

import com.yoursway.model.repository.IModelElement;
import com.yoursway.model.resource.IResourceFile;

public interface IFileBasedElement extends IModelElement {
    
    IResourceFile getFile();
    
}
