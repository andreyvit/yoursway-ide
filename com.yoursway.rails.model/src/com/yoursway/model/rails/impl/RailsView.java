package com.yoursway.model.rails.impl;

import com.yoursway.model.rails.IRailsView;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceFile;

public class RailsView implements IRailsView {
    
    public IResourceFile getFile() {
        return new IResourceFile() {
            
            public IHandle<String> name() {
                return new RabbitHandle<String>("index.html");
            }
            
        };
    }
    
}
