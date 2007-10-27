package com.yoursway.model.rails;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;
import com.yoursway.model.resource.IResourceProject;

public interface IRailsApplicationProject extends IModelElement {
    
    IResourceProject getResourceProject();
    
    IHandle<Collection<IRailsController>> controllers();
    
    IHandle<Collection<IRailsModel>> models();
    
    IHandle<Collection<IRailsMigration>> migrations();
    
    IHandle<Collection<IRailsTest>> tests();
    
    IHandle<Collection<IRailsFixture>> fixtures();
    
    IHandle<Collection<IRailsPlugin>> installedPlugins();
    
    IHandle<Collection<IRailsHelper>> helpers();
    
    IHandle<Collection<IRailsPartial>> sharedPartials();
    
    IHandle<Collection<IRailsLayout>> layouts();
    
}
