package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IRailsTest extends IModelElement, IFileBasedElement {
    public static final int FUNCTIONAL = 0;
    public static final int UNIT = 1;
    
    IHandle<Integer> getKind();
    
}
