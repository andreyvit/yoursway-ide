package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.rails.model.IRailsControllersCollection;
import com.yoursway.rails.model.IRailsProject;

public class RailsProject implements IRailsProject {
    
    private final IProject project;
    
    private RailsControllersCollection controllers;
    
    private final RailsProjectsCollection parent;
    
    public RailsProject(RailsProjectsCollection parent, IProject project) {
        this.parent = parent;
        this.project = project;
    }
    
    public void refresh() {
        controllers.refresh();
    }
    
    public IProject getProject() {
        return project;
    }
    
    public IRailsControllersCollection getControllersCollection() {
        if (controllers == null) {
            controllers = new RailsControllersCollection(this);
        }
        return controllers;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        controllers.reconcile(deltaBuilder, delta);
    }
    
}
