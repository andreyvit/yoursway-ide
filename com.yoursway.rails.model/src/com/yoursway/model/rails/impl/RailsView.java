package com.yoursway.model.rails.impl;

import com.yoursway.model.rails.IRailsView;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IAST;
import com.yoursway.model.resource.IResourceFile;

public class RailsView implements IRailsView {
    
    public IHandle<IResourceFile> getFile() {
        return new RabbitHandle<IResourceFile>(new IResourceFile() {
            
            public IHandle<IAST> ast() {
                // TODO Auto-generated method stub
                return null;
            }
            
            public IHandle<String> getName() {
                return new RabbitHandle<String>("index.html");
            }
            
        });
    }
    
}
