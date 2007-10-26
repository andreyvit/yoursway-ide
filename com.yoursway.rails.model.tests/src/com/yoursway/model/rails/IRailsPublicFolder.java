package com.yoursway.model.rails;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IModelElement;
import com.yoursway.model.sample.IResourceFile;

public interface IRailsPublicFolder extends IModelElement, IFileBasedElement {
    ICollectionHandle<IResourceFile> getFiles();
}
