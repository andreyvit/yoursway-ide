package com.yoursway.model.rails.impl;

import org.eclipse.core.runtime.IPath;

import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsFixture;
import com.yoursway.model.rails.IRailsHelper;
import com.yoursway.model.rails.IRailsLayout;
import com.yoursway.model.rails.IRailsMigration;
import com.yoursway.model.rails.IRailsModel;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsPlugin;
import com.yoursway.model.rails.IRailsProject;
import com.yoursway.model.rails.IRailsPublicFolder;
import com.yoursway.model.rails.IRailsTest;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceProject;

public class RailsProject implements IRailsProject {

    public RailsProject() {
    }
    
    public ICollectionHandle<IRailsController> getControllers() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsFixture> getFixtures() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsHelper> getHelpers() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsLayout> getLayouts() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsMigration> getMigrations() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsModel> getModels() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<String> getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<IPath> getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsPlugin> getPlugins() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<IRailsPublicFolder> getPublic() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<IResourceProject> getResourceProject() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsPartial> getSharedPartials() {
        // TODO Auto-generated method stub
        return null;
    }

    public ICollectionHandle<IRailsTest> getTests() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
