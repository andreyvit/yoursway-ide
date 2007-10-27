package com.yoursway.model.rails.impl;

import java.util.Collections;

import com.yoursway.model.rails.IRailsModelRoot;
import com.yoursway.model.rails.IRailsPlugin;
import com.yoursway.model.rails.IRailsProject;
import com.yoursway.model.repository.ICollectionHandle;

public class RailsModelRoot implements IRailsModelRoot {

    public RailsModelRoot() {
    }
    
    public ICollectionHandle<IRailsPlugin> getPlugins() {
        return null;
    }

    public ICollectionHandle<IRailsProject> projects() {
        return new RabbitFamilyHandle<IRailsProject>(Collections.singleton((IRailsProject)new RailsProject()));
    }
    
}
