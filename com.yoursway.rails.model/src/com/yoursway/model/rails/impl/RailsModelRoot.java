package com.yoursway.model.rails.impl;

import java.util.Collection;
import java.util.Collections;

import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.rails.IRailsModelRoot;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceProject;

public class RailsModelRoot implements IRailsModelRoot {
    
    public RailsModelRoot() {
    }
    
    public IHandle<Collection<IRailsApplicationProject>> projects() {
        return new RabbitFamilyHandle<IRailsApplicationProject>(Collections
                .singleton((IRailsApplicationProject) new RailsProject()));
    }
    
    public IHandle<IRailsApplicationProject> mapProject(IResourceProject resourceProject) {
        return null;
    }
    
}
