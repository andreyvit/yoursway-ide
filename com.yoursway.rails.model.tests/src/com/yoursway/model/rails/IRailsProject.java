package com.yoursway.model.rails;

import org.eclipse.core.runtime.IPath;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.sample.IResourceProject;

public interface IRailsProject {
    
    IHandle<String> getName();
    
    IHandle<IPath> getPath();
    
    IHandle<IResourceProject> getResourceProject();

    ICollectionHandle<IRailsController> getControllers();
    
    ICollectionHandle<IRailsModel> getModels();
    
    ICollectionHandle<IRailsMigration> getMigrations();
    
    ICollectionHandle<IRailsTest> getTests();
    
    ICollectionHandle<IRailsFixture> getFixtures();
    
    ICollectionHandle<IRailsPlugin> getPlugins();
    
    IHandle<IRailsPublicFolder> getPublic();
    
    ICollectionHandle<IRailsHelper> getHelpers();
    
    ICollectionHandle<IRailsPartial> getSharedPartials();
    
    ICollectionHandle<IRailsLayout> getLayouts();
    
}
