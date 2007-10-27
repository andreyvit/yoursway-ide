package com.yoursway.rails.model;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;

import com.yoursway.databinding.commons.YourSwayRealm;
import com.yoursway.rails.commons.Inflector;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.model.internal.ControllersObservableSet;

public class RailsProject {
    
    private final IProject project;
    private final Inflector inflector;
    
    private ControllersObservableSet controllers;
    
    public RailsProject(YourSwayRealm realm, IProject project) {
        Assert.isLegal(project != null);
        this.project = project;
        inflector = RailsNamingConventions.createInitializedInflector();
        controllers = new ControllersObservableSet(realm, this);
    }
    
    public IProject getProject() {
        return project;
    }
    
    public Inflector getInflector() {
        return inflector;
    }
    
    public IObservableSet getControllers() {
        return controllers;
    }
    
}
