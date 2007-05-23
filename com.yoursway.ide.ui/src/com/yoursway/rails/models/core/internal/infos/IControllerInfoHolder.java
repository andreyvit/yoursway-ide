package com.yoursway.rails.models.core.internal.infos;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.controller.RailsController;

public interface IControllerInfoHolder {
    
    public RailsController get(Object key);
    
    public Collection<RailsController> values();
    
    public RailsController remove(IFile file);
    
    public void add(RailsController info);
    
    public void removeAll(HashSet<RailsController> items);
    
}
