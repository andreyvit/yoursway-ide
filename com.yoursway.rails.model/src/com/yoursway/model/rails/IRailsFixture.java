package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;

public interface IRailsFixture extends IFileBasedElement {
    
    IHandle<String> tableName();
    
}
