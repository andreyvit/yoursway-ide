package com.yoursway.model.rails;

import com.yoursway.model.repository.ICollectionHandle;

public interface IRailsModelRoot {
    
    ICollectionHandle<IRailsProject> projects();
    
    ICollectionHandle<IRailsPlugin> getPlugins();
    
}
