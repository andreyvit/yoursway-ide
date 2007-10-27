package com.yoursway.model.rails.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsFixture;
import com.yoursway.model.rails.IRailsHelper;
import com.yoursway.model.rails.IRailsLayout;
import com.yoursway.model.rails.IRailsMigration;
import com.yoursway.model.rails.IRailsModel;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsPlugin;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.rails.IRailsPublicFolder;
import com.yoursway.model.rails.IRailsTest;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceProject;

public class RailsProject implements IRailsApplicationProject {

    public RailsProject() {
    }
    
    public IHandle<Collection<IRailsController>> getControllers() {
        List<IRailsController> list = new ArrayList<IRailsController>();
        list.add(new RailsController("Piece Of Shit"));
        return new RabbitFamilyHandle<IRailsController>(list);
    }

    public IHandle<Collection<IRailsFixture>> getFixtures() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<Collection<IRailsHelper>> getHelpers() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<Collection<IRailsLayout>> getLayouts() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<Collection<IRailsMigration>> getMigrations() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<Collection<IRailsModel>> getModels() {
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

    public IHandle<Collection<IRailsPlugin>> installedPlugins() {
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

    public IHandle<Collection<IRailsPartial>> getSharedPartials() {
        // TODO Auto-generated method stub
        return null;
    }

    public IHandle<Collection<IRailsTest>> getTests() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
