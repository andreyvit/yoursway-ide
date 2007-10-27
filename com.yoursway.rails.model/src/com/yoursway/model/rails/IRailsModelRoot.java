package com.yoursway.model.rails;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.resource.IResourceProject;

public interface IRailsModelRoot extends IModelRoot {
    
    ICollectionHandle<IRailsApplicationProject> projects();
    
    IHandle<IRailsApplicationProject> mapProject(IResourceProject resourceProject);
    
}
