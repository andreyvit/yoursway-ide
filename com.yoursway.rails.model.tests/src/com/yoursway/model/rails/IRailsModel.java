package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IRailsModel extends IModelElement, IFileBasedElement {
    IHandle<String> getName();
}
