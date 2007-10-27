package com.yoursway.model.rails.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsControllerAction;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsView;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceFile;

public class RailsController implements IRailsController {
    
    private final String name;
    
    public RailsController(String name) {
        this.name = name;
    }
    
    public IHandle<Collection<IRailsControllerAction>> getActions() {
        List<IRailsControllerAction> list = new ArrayList<IRailsControllerAction>();
        list.add(new RailsControllerAction("Piece Of Shit"));
        return new RabbitFamilyHandle<IRailsControllerAction>(list);
    }
    
    public IHandle<String> getName() {
        return new RabbitHandle<String>(name);
    }
    
    public IHandle<Collection<IRailsPartial>> getPartials() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsView>> getViews() {
        return new RabbitFamilyHandle<IRailsView>(Collections.singleton((IRailsView)new RailsView()));
    }
    
    public IHandle<IResourceFile> getFile() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
