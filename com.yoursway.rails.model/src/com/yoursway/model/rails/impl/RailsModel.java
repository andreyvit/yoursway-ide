package com.yoursway.model.rails.impl;

import com.yoursway.model.rails.IRailsModel;
import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceFile;

public class RailsModel implements IRailsModel {
    
    private final IConventionalClassName name;
    
    public RailsModel(IConventionalClassName name) {
        this.name = name;
    }
    
    public IHandle<IConventionalClassName> name() {
        return new RabbitHandle<IConventionalClassName>(name);
    }
    
    public IResourceFile getFile() {
        return null;
    }
    
}
