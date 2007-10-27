package com.yoursway.model.rails;

import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.repository.IHandle;

public interface IRailsMigration extends IFileBasedElement {
    
    IHandle<Integer> ordinal();
    
    IHandle<IConventionalClassName> name();
    
}
