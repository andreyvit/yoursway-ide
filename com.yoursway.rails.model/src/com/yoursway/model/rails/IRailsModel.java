package com.yoursway.model.rails;

import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.repository.IHandle;

public interface IRailsModel extends IFileBasedElement {
    
    IHandle<IConventionalClassName> name();
    
}
