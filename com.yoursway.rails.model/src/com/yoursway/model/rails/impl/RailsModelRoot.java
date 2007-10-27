package com.yoursway.model.rails.impl;

import java.util.Collections;

import com.yoursway.model.rails.IRailsModelRoot;
import com.yoursway.model.rails.IRailsPlugin;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.repository.ICollectionHandle;

public class RailsModelRoot implements IRailsModelRoot {

    public RailsModelRoot() {
    }
    
    public ICollectionHandle<IRailsPlugin> getPlugins() {
        return null;
    }

    public ICollectionHandle<IRailsApplicationProject> projects() {
        return new RabbitFamilyHandle<IRailsApplicationProject>(Collections.singleton((IRailsApplicationProject)new RailsProject()));
    }
    
}
