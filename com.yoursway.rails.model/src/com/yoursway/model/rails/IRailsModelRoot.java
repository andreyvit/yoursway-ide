package com.yoursway.model.rails;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IModelRoot;

public interface IRailsModelRoot extends IModelRoot {
    
    ICollectionHandle<IRailsProject> projects();
    
    ICollectionHandle<IRailsPlugin> getPlugins();
    
}
