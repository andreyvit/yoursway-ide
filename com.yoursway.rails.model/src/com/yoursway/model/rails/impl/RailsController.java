package com.yoursway.model.rails.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsControllerAction;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsView;
import com.yoursway.model.rails.conventionalClassNames.ConsistentConventionalClassName;
import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.rails.conventionalClassNames.IConventionalClassNameVisitor;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceFile;

public class RailsController implements IRailsController {
    
    private final String name;
    
    public RailsController(String name) {
        this.name = name;
    }
    
    public IHandle<IConventionalClassName> name() {
        return new RabbitHandle<IConventionalClassName>(new IConventionalClassName() {
            
            public void accept(IConventionalClassNameVisitor visitor) {
                visitor.visitConsistentName(new ConsistentConventionalClassName(name, null));
            }
            
        });
    }
    
    public IHandle<Collection<IRailsPartial>> partials() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IHandle<Collection<IRailsView>> views() {
        return new RabbitFamilyHandle<IRailsView>(Collections.singleton((IRailsView) new RailsView()));
    }
    
    public IHandle<Collection<IRailsControllerAction>> actions() {
        List<IRailsControllerAction> list = new ArrayList<IRailsControllerAction>();
        list.add(new RailsControllerAction("die"));
        return new RabbitFamilyHandle<IRailsControllerAction>(list);
    }
    
    public IResourceFile getFile() {
        return null;
    }
    
}
