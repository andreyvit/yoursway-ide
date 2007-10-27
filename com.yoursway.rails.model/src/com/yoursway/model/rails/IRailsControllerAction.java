package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IRailsControllerAction extends IModelElement {
    
    IHandle<String> getName();
    
}
