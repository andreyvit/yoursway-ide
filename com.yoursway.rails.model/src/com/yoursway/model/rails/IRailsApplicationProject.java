package com.yoursway.model.rails;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IModelElement;
import com.yoursway.model.resource.IResourceProject;

public interface IRailsApplicationProject extends IModelElement {
    
    IResourceProject getResourceProject();
    
    ICollectionHandle<IRailsController> controllers();
    
    ICollectionHandle<IRailsModel> models();
    
    ICollectionHandle<IRailsMigration> migrations();
    
    ICollectionHandle<IRailsTest> tests();
    
    ICollectionHandle<IRailsFixture> fixtures();
    
    ICollectionHandle<IRailsPlugin> installedPlugins();
    
    ICollectionHandle<IRailsHelper> helpers();
    
    ICollectionHandle<IRailsPartial> sharedPartials();
    
    ICollectionHandle<IRailsLayout> layouts();
    
}
