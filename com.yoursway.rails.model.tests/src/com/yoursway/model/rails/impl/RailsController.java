package com.yoursway.model.rails.impl;

import java.util.Collections;

import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsControllerAction;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsView;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.sample.IResourceFile;

public class RailsController implements IRailsController {
    
    private final String name;
    
    public RailsController(String name) {
        this.name = name;
    }
    
    public ICollectionHandle<IRailsControllerAction> getActions() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<String> getName() {
        return new RabbitHandle<String>(name);
    }
    
    public ICollectionHandle<IRailsPartial> getPartials() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ICollectionHandle<IRailsView> getViews() {
        return new RabbitFamilyHandle<IRailsView>(Collections.singleton((IRailsView)new RailsView()));
    }
    
    public IHandle<IResourceFile> getFile() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
