package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.sample.IResourceFile;

public interface IFileBasedElement {
    IHandle<IResourceFile> getFile();
}
