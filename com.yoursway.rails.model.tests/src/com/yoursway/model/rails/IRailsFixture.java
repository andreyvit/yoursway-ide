package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IRailsFixture  extends IModelElement, IFileBasedElement {
    
    IHandle<String> getTableName();
    
}
