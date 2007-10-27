package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;

public interface IRailsTest extends IFileBasedElement {
    
    public enum TestKind {
        FUNCTIONAL, UNIT
    }
    
    IHandle<TestKind> kind();
    
}
