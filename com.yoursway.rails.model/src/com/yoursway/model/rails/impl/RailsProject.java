package com.yoursway.model.rails.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsFixture;
import com.yoursway.model.rails.IRailsHelper;
import com.yoursway.model.rails.IRailsLayout;
import com.yoursway.model.rails.IRailsMigration;
import com.yoursway.model.rails.IRailsModel;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsPlugin;
import com.yoursway.model.rails.IRailsTest;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceProject;

public class RailsProject implements IRailsApplicationProject {
    
    public RailsProject() {
    }
    
    public IHandle<Collection<IRailsController>> controllers() {
        List<IRailsController> list = new ArrayList<IRailsController>();
        list.add(new RailsController("Piece Of Shit"));
        return new RabbitFamilyHandle<IRailsController>(list);
    }
    
    public IHandle<Collection<IRailsFixture>> fixtures() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsHelper>> helpers() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsLayout>> layouts() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsMigration>> migrations() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsModel>> models() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<String> name() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<IPath> path() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsPlugin>> installedPlugins() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IResourceProject getResourceProject() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsTest>> tests() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsPartial>> sharedPartials() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
