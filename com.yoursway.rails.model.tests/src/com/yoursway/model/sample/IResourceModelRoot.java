package com.yoursway.model.sample;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IModelRoot;


public interface IResourceModelRoot extends IModelRoot {
    
    ICollectionHandle<IResourceProject> projects();
    
}
