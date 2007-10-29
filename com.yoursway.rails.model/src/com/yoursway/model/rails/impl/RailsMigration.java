package com.yoursway.model.rails.impl;

import com.yoursway.model.rails.IRailsMigration;
import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceFile;

public class RailsMigration implements IRailsMigration {
    
    private final IConventionalClassName name;
    private final int ordinal;
    
    public RailsMigration(IConventionalClassName name, int ord) {
        this.name = name;
        this.ordinal = ord;
    }
    
    public IHandle<IConventionalClassName> name() {
        return new RabbitHandle<IConventionalClassName>(name);
    }
    
    public IHandle<Integer> ordinal() {
        return new RabbitHandle<Integer>(ordinal);
    }
    
    public IResourceFile getFile() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
